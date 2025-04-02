package com.goose_n5.mindcraft.screen;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends Screen {
    private List<Question> questions;
    private List<TextFieldWidget> questionFields;
    private Screen parent;

    public ConfigScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
        loadQuestions();
    }


    public class Question {
        private String question;
        private List<String> answers;
        private int correct;

        // Getters and setters
        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public List<String> getAnswers() {
            return answers;
        }

        public void setAnswers(List<String> answers) {
            this.answers = answers;
        }

        public int getCorrect() {
            return correct;
        }

        public void setCorrect(int correct) {
            this.correct = correct;
        }
    }

    private void loadQuestions() {
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("/assets/mindcraft/questions.json"))) {
            Type questionListType = new TypeToken<List<Question>>() {}.getType();
            questions = new Gson().fromJson(reader, questionListType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void init() {
        super.init();
        questionFields = new ArrayList<>();
        int y = 20;
        for (Question question : questions) {
            System.out.println(question.getQuestion());
            /*TextFieldWidget questionField = new TextFieldWidget(textRenderer, 20, y, 200, 20, Text.of(question.getQuestion()));
            questionField.setEditable(false);
            addDrawableChild(questionField);
            questionFields.add(questionField);*/
            y += 30;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        int y = 20;
        for (Question question : questions) {
            context.drawText(this.textRenderer, question.getQuestion(), 20, y, 0xFFFFFF, true);
            y += 20;
        }
        /*for (TextFieldWidget questionField : questionFields) {
            questionField.render(context, mouseX, mouseY, delta);
        }*/
    }

    @Override
    public void close() {
        super.close();
    }
}