package com.goose_n5.mindcraft.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class QuestionListScreen extends Screen {
    private final Screen parent;

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

        for (int i = 0; i < MindCraftScreen.getQuestions().size(); i++) {
            int index = i;
            ButtonWidget removeButton = ButtonWidget.builder(Text.literal("Remove"), button -> {
                MindCraftScreen.getQuestions().remove(index);
                MindCraftScreen.saveQuestions();
                this.init();
            }).dimensions(buttonX + 210, buttonY + (i * 30), 80, buttonHeight).build();

            this.addDrawableChild(removeButton);
        }

        ButtonWidget addButton = ButtonWidget.builder(Text.literal("Add Question"), button -> {
            //TODO redirect to an add question screen
        }).dimensions(buttonX, buttonY + (MindCraftScreen.getQuestions().size() * 30) + 30, buttonWidth, buttonHeight).build();

        this.addDrawableChild(addButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int questionY = 30;
        for (int i = 0; i < MindCraftScreen.getQuestions().size(); i++) {
            String questionText = MindCraftScreen.getQuestions().get(i).getQuestion();
            context.drawText(this.textRenderer, questionText, 30, questionY + (i * 30), 0xFFFFFF, false);
        }
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }
}