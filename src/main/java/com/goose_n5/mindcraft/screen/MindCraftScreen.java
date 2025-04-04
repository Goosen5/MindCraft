package com.goose_n5.mindcraft.screen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.reflect.TypeToken;
import com.goose_n5.mindcraft.MindCraft;
import com.goose_n5.mindcraft.screen.components.Question;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Screen for the MindCraft game.
 */
public class MindCraftScreen extends Screen {

    public static final int COOLDOWN_TIME = 180;
    private static long cooldownEndTime = 0;
    private static boolean isCooldownActive = false;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private List<String> rewards;

    private Screen parent;
    private static List<Question> questions;
    private static int currentQuestionIndex = -1;
    private final Random random = new Random();

    private static final int MESSAGE_DISPLAY_TIME = 3000;
    private String message = "";
    private long messageEndTime = 0;
    private boolean newQuestionNeeded = true;



    /**
     * Creates a new MindCraftScreen.
     *
     * @param title The title of the screen.
     * @param parent The parent screen.
     */
    public MindCraftScreen(Text title ,Screen parent) {
        super(title);
        this.parent = parent;
        loadQuestions();
        loadRewards();
        selectRandomQuestion();
        startCooldownChecker();
    }



    /**
     * Loads the questions from the questions.json file.
     */
    static void loadQuestions(){
        Gson gson = new Gson();
        Type rewardListType = new TypeToken<List<Question>>(){}.getType(); // TypeToken is a class that represents a generic type.
        File questionsFile = new File(MindCraft.CONFIG_DIR,"questions.json");

        try (FileReader reader = new FileReader(questionsFile)) {
            questions = gson.fromJson(reader, rewardListType);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves the questions to the questions.json file.
     */
    public static void saveQuestions(){
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting(); // Makes the JSON output more human-readable.
        Gson gson = builder.create();
        File questionsFile = new File(MindCraft.CONFIG_DIR,"questions.json");

        try (FileWriter writer = new FileWriter(questionsFile)) {
            gson.toJson(questions, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a question to the list of questions.
     *
     * @param question The question to add.
     */
    public void addQuestion(Question question){
        questions.add(question);
        saveQuestions();
    }

    /**
     * Removes a question from the list of questions.
     *
     * @param index The index of the question to remove.
     */
    public void removeQuestion(int index){
        if (index >= 0 && index < questions.size()){
            questions.remove(index);
            saveQuestions();
        }
    }

    /**
     * Loads the rewards from the rewards.json file.
     */
    private void loadRewards(){
        Gson gson = new Gson();
        Type rewardListType = new TypeToken<List<String>>(){}.getType();
        // InputStreamReader is a bridge from byte streams to character streams.
        InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/assets/mindcraft/rewards.json")));
        rewards = gson.fromJson(reader, rewardListType);
    }

    /**
     * Selects a random question from the list of questions.
     */
    private void selectRandomQuestion(){
        currentQuestionIndex = random.nextInt(questions.size());
    }

    /**
     * Selects a random reward from the list of rewards.
     *
     * @return The selected reward.
     */
    public String selectRandomReward(){
        return rewards.get(random.nextInt(rewards.size()));
    }

    /**
     * Gives the player a reward.
     */
    public void giveReward(){
        String reward = selectRandomReward();
        String[] parts = reward.split(" ");
        String itemName = parts[0];
        int quantity = parts.length > 1 ? Integer.parseInt(parts[1]) : 1; // If the reward has a quantity, parse it. Otherwise, default to 1.

        // Create an ItemStack with the reward item and quantity.
        ItemStack itemStack = new ItemStack(Registries.ITEM.get(new Identifier(itemName)), quantity);
        MinecraftClient.getInstance().player.giveItemStack(itemStack);
    }

    /**
     * Starts the cooldown timer.
     */
    private void startCooldown(){
        isCooldownActive = true;
        cooldownEndTime = System.currentTimeMillis() + COOLDOWN_TIME * 1000;
    }

    /**
     * Starts the cooldown checker.
     */
    private void startCooldownChecker(){
        scheduler.scheduleAtFixedRate(() -> {
            if (isCooldownActive && System.currentTimeMillis() >= cooldownEndTime){
                isCooldownActive = false;
                newQuestionNeeded = true;
                MinecraftClient.getInstance().player.sendMessage(Text.literal("A new question is available"), false);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Sets the message to display on the screen.
     *
     * @param message The message to display.
     */
    private void setMessage(String message){
        this.message = message;
        this.messageEndTime = System.currentTimeMillis() + MESSAGE_DISPLAY_TIME;
    }



    @Override
    protected void init() {
        if (newQuestionNeeded){
            selectRandomQuestion();
            newQuestionNeeded = false;
        }

        int buttonWidth = 200;
        int buttonHeight = 20;
        int buttonX = (this.width - buttonWidth) / 2;
        int buttonY = (this.height - buttonHeight) / 2;

        ButtonWidget answerButton1 = ButtonWidget.builder(Text.literal(questions.get(currentQuestionIndex).getAnswers().get(0)), button -> checkAnswer(0))
                .dimensions(buttonX, buttonY, buttonWidth, buttonHeight)
                .build();

        ButtonWidget answerButton2 = ButtonWidget.builder(Text.literal(questions.get(currentQuestionIndex).getAnswers().get(1)), button -> checkAnswer(1))
                .dimensions(buttonX, buttonY + 30, buttonWidth, buttonHeight)
                .build();

        ButtonWidget answerButton3 = ButtonWidget.builder(Text.literal(questions.get(currentQuestionIndex).getAnswers().get(2)), button -> checkAnswer(2))
                .dimensions(buttonX, buttonY + 60, buttonWidth, buttonHeight)
                .build();

        this.addDrawableChild(answerButton1);
        this.addDrawableChild(answerButton2);
        this.addDrawableChild(answerButton3);
    }

    /**
     * Checks the player's answer.
     *
     * @param answer The player's answer.
     */
    private void checkAnswer(int answer){
        if (isCooldownActive){
            MinecraftClient.getInstance().player.sendMessage(Text.literal("You need to wait before answering again"), false);
            return;
        }

        boolean isCorrect = (answer == questions.get(currentQuestionIndex).getCorrect());

        if (isCorrect){
            setMessage("§a Correct");
            giveReward();
        } else {
            setMessage("§c Incorrect");
        }

        startCooldown();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        if (newQuestionNeeded){
            return;
        }

        // Draw the question text in the center of the screen.
        int questionX = (this.width - this.textRenderer.getWidth(questions.get(currentQuestionIndex).getQuestion())) / 2;
        int questionY = (this.height - this.textRenderer.fontHeight) / 2 - 30;
        context.drawText(this.textRenderer, questions.get(currentQuestionIndex).getQuestion(), questionX, questionY, 0xFFFFFF, true);

        if (isCooldownActive){
            long remainingTime = (cooldownEndTime - System.currentTimeMillis()) / 1000;
            String cooldownText = "§c Cooldown: " + remainingTime + "s";
            context.drawText(this.textRenderer, cooldownText, this.width - this.textRenderer.getWidth(cooldownText) - 10, 10, 0xFFFFFF, true);
        }

        // Draw the message in the center of the screen.
        if (System.currentTimeMillis() < messageEndTime){
            int messageWidth = this.textRenderer.getWidth(message);
            int messageX = (this.width - messageWidth) / 2;
            int messageY = this.height / 2 -70;
            context.drawText(this.textRenderer, Text.literal(message).formatted(Formatting.BOLD), messageX, messageY, 0xFFFFFF, true);
        }else if (!message.isEmpty()){
            message = "";
            this.close();
        }
    }

    public static List<Question> getQuestions() {
        return questions;
    }

    @Override
    public void close() {
        super.close();
    }
}