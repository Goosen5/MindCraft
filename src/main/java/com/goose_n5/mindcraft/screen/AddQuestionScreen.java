package com.goose_n5.mindcraft.screen;

import com.goose_n5.mindcraft.screen.components.Question;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Screen for adding a question.
 */
public class AddQuestionScreen extends Screen {
    private final Screen parent;
    private TextFieldWidget questionField;
    private TextFieldWidget answerField1;
    private TextFieldWidget answerField2;
    private TextFieldWidget answerField3;
    private TextFieldWidget correctField;

    /**
     * Creates a new AddQuestionScreen.
     *
     * @param parent The parent screen.
     */
    public AddQuestionScreen(Screen parent) {
        super(Text.literal("Add Question"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int buttonWidth = 200;
        int buttonHeight = 20;
        int buttonX = (this.width - buttonWidth) / 2;
        int buttonY = 30;

        questionField = new TextFieldWidget(this.textRenderer, buttonX, buttonY, buttonWidth, buttonHeight, Text.literal("Question"));
        this.addDrawableChild(questionField);

        answerField1 = new TextFieldWidget(this.textRenderer, buttonX, buttonY + 30, buttonWidth, buttonHeight, Text.literal("Answer 1"));
        this.addDrawableChild(answerField1);

        answerField2 = new TextFieldWidget(this.textRenderer, buttonX, buttonY + 60, buttonWidth, buttonHeight, Text.literal("Answer 2"));
        this.addDrawableChild(answerField2);

        answerField3 = new TextFieldWidget(this.textRenderer, buttonX, buttonY + 90, buttonWidth, buttonHeight, Text.literal("Answer 3"));
        this.addDrawableChild(answerField3);

        correctField = new TextFieldWidget(this.textRenderer, buttonX, buttonY + 120, buttonWidth, buttonHeight, Text.literal("Correct Answer (0, 1, or 2)"));
        this.addDrawableChild(correctField);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Save"), button -> {
            String questionText = questionField.getText();
            String answer1 = answerField1.getText();
            String answer2 = answerField2.getText();
            String answer3 = answerField3.getText();
            int correctAnswer = Integer.parseInt(correctField.getText());

            if (!questionText.isEmpty() && !answer1.isEmpty() && !answer2.isEmpty() && !answer3.isEmpty()) {
                List<String> answers = new ArrayList<>();
                answers.add(answer1);
                answers.add(answer2);
                answers.add(answer3);

                Question question = new Question();
                question.setQuestion(questionText);
                question.setAnswers(answers);
                question.setCorrect(correctAnswer);

                MindCraftScreen.getQuestions().add(question);
                MindCraftScreen.saveQuestions();
                MinecraftClient.getInstance().setScreen(parent);
            }
        }).dimensions(buttonX, buttonY + 160, buttonWidth, buttonHeight).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), button -> {
            MinecraftClient.getInstance().setScreen(parent);
        }).dimensions(buttonX, buttonY + 190, buttonWidth, buttonHeight).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        questionField.render(context, mouseX, mouseY, delta);
        answerField1.render(context, mouseX, mouseY, delta);
        answerField2.render(context, mouseX, mouseY, delta);
        answerField3.render(context, mouseX, mouseY, delta);
        correctField.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }
}