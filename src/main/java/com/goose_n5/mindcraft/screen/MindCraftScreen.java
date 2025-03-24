package com.goose_n5.mindcraft.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MindCraftScreen extends Screen {

    public Screen parent;

    public MindCraftScreen(Text title ,Screen parent) {
        super(title);
        this.parent = parent;
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

    @Override
    public void close() {
        super.close();
    }
}