package com.goose_n5.mindcraft.screen;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class MindCraftScreen extends Screen {

    private Screen parent;
    private List<Question> questions;

    public MindCraftScreen(Text title ,Screen parent) {
        super(title);
        this.parent = parent;
        loadQuestions();
    }

    private static class Question{
        String question;
        List<String> answers;
        int correct;
    }

    @Override
    protected void init() {
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int textX = (this.width - this.textRenderer.getWidth("Screen")) / 2;
        int textY = (this.height - this.textRenderer.fontHeight) / 2;
        context.drawText(this.textRenderer, "Screen", textX, textY, 0xFFFFFF, true);
        System.out.println("Screen");
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