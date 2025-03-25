package com.goose_n5.mindcraft.screen;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MindCraftScreen extends Screen {

    private Screen parent;
    private List<Question> questions;
    private static int currentQuestionIndex = -1;
    private final Random random = new Random();

    public MindCraftScreen(Text title ,Screen parent) {
        super(title);
        this.parent = parent;
        loadQuestions();
        selectRandomQuestion();
    }

    private void selectRandomQuestion(){
        currentQuestionIndex = random.nextInt(questions.size());
    }

    private static class Question{
        String question;
        List<String> answers;
        int correct;
    }

    @Override
    protected void init() {
        int buttonWidth = 200;
        int buttonHeight = 20;
        int buttonX = (this.width - buttonWidth) / 2;
        int buttonY = (this.height - buttonHeight) / 2;

        ButtonWidget answerButton1 = ButtonWidget.builder(Text.literal(questions.get(currentQuestionIndex).answers.get(0)), button -> checkAnswer(0))
                .dimensions(buttonX, buttonY, buttonWidth, buttonHeight)
                .build();

        ButtonWidget answerButton2 = ButtonWidget.builder(Text.literal(questions.get(currentQuestionIndex).answers.get(1)), button -> checkAnswer(1))
                .dimensions(buttonX, buttonY + 30, buttonWidth, buttonHeight)
                .build();

        ButtonWidget answerButton3 = ButtonWidget.builder(Text.literal(questions.get(currentQuestionIndex).answers.get(2)), button -> checkAnswer(2))
                .dimensions(buttonX, buttonY + 60, buttonWidth, buttonHeight)
                .build();

        this.addDrawableChild(answerButton1);
        this.addDrawableChild(answerButton2);
        this.addDrawableChild(answerButton3);
    }

    private void checkAnswer(int answer){
        boolean isCorrect = (answer == questions.get(currentQuestionIndex).correct);

        if (isCorrect){
            System.out.println("Correct");
        } else {
            System.out.println("Incorrect");
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int questionX = (this.width - this.textRenderer.getWidth(questions.get(currentQuestionIndex).question)) / 2;
        int questionY = (this.height - this.textRenderer.fontHeight) / 2 - 30;
        context.drawText(this.textRenderer, questions.get(currentQuestionIndex).question, questionX, questionY, 0xFFFFFF, true);
    }

    private void loadQuestions(){
        Gson gson = new Gson();
        Type rewardListType = new TypeToken<List<Question>>(){}.getType();
        InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/assets/mindcraft/questions.json")));
        questions = gson.fromJson(reader, rewardListType);
        System.out.println(questions);
    }

    @Override
    public void close() {
        super.close();
    }
}