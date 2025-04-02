package com.goose_n5.mindcraft.screen;

import com.goose_n5.mindcraft.screen.components.Question;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;


/**
 * Screen for displaying a list of questions.
 */
public class QuestionListScreen extends Screen {
    private final Screen parent;
    private static final int QUESTIONS_PER_PAGE = 6;
    private int currentPage = 0;


    /**
     * Creates a new QuestionListScreen.
     *
     * @param parent The parent screen.
     */
    public QuestionListScreen(Screen parent) {
        super(Text.literal("Question List"));
        this.parent = parent;
        MindCraftScreen.loadQuestions();
    }

    @Override
    protected void init() {
        int buttonWidth = 200;
        int buttonHeight = 20;
        int buttonX = (this.width - buttonWidth) / 2;
        int buttonY = 30;

        // Clear existing children
        this.clearChildren();

        // Get questions for the current page
        List<Question> questions = MindCraftScreen.getQuestions();
        int start = currentPage * QUESTIONS_PER_PAGE;
        int end = Math.min(start + QUESTIONS_PER_PAGE, questions.size());

        for (int i = start; i < end; i++) {
            String questionText = questions.get(i).getQuestion();
            int y = buttonY + (i - start) * 30;

            int I = i;
            // Add a button to remove the question
            this.addDrawableChild(ButtonWidget.builder(Text.literal("Remove"), button -> {
                questions.remove(I);
                MindCraftScreen.saveQuestions();
                this.init();
            }).dimensions(buttonX + buttonWidth + 10, y, 80, buttonHeight).build());
        }

        // Add buttons to navigate between pages
        if (currentPage > 0) {
            this.addDrawableChild(ButtonWidget.builder(Text.literal("Previous"), button -> {
                currentPage--;
                this.init();
            }).dimensions(buttonX - 100, this.height - 30, 80, 20).build());
        }

        // Add a button to go to the next page
        if (end < questions.size()) {
            this.addDrawableChild(ButtonWidget.builder(Text.literal("Next"), button -> {
                currentPage++;
                this.init();
            }).dimensions(buttonX + buttonWidth + 20, this.height - 30, 80, 20).build());
        }

        // Add a button to add a new question
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Add Question"), button -> {
            MinecraftClient.getInstance().setScreen(new AddQuestionScreen(this)); // Change the screen to AddQuestionScreen
        }).dimensions(buttonX, this.height - 30, buttonWidth, buttonHeight).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        List<Question> questions = MindCraftScreen.getQuestions();
        int start = currentPage * QUESTIONS_PER_PAGE;
        int end = Math.min(start + QUESTIONS_PER_PAGE, questions.size());

        int textX = 30;
        int textY = 30;

        // Draw the question text for each question
        for (int i = start; i < end; i++) {
            String questionText = questions.get(i).getQuestion();
            context.drawText(this.textRenderer, questionText, textX, textY + (i - start) * 30, 0xFFFFFF, false);
        }
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }
}