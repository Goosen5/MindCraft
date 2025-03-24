package com.goose_n5.mindcraft.event;

import com.goose_n5.mindcraft.screen.MindCraftScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeybindManager {
    public static final String KEYBIND_CATEGORY = "key.category.mindcraft";
    public static final String KEYBIND_OPEN_GUI = "key.mindcraft.open_gui";

    public static KeyBinding openGui;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openGui.wasPressed()) {
                System.out.println("Open GUI key was pressed");
                Screen currentScreen = MinecraftClient.getInstance().currentScreen;
                MinecraftClient.getInstance().setScreen(
                        new MindCraftScreen(Text.empty(),currentScreen)
                );
            }
        });
    }

    public static void register() {
        openGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEYBIND_OPEN_GUI,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                KEYBIND_CATEGORY
        ));
    }
}