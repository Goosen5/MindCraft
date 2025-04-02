package com.goose_n5.mindcraft.event;

import com.goose_n5.mindcraft.screen.MindCraftScreen;
import com.goose_n5.mindcraft.screen.QuestionListScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/**
 * Manages keybinds for the mod.
 */
public class KeybindManager {
    public static final String KEYBIND_CATEGORY = "key.category.mindcraft";
    public static final String KEYBIND_OPEN_GUI = "key.mindcraft.open_gui";
    public static final String KEYBIND_OPEN_CONFIG = "key.mindcraft.open_config";

    public static KeyBinding openGui;
    public static KeyBinding openConfig;

    public static void registerKeyInputs() {
        // Register key inputs
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Open the GUI
            if (openGui.wasPressed()) {
                System.out.println("Open GUI key was pressed");
                Screen currentScreen = MinecraftClient.getInstance().currentScreen;
                MinecraftClient.getInstance().setScreen(
                        new MindCraftScreen(Text.empty(),currentScreen)
                );
            }
            // Open the config screen
            if (openConfig.wasPressed()) {
                System.out.println("Open Config key was pressed");
                Screen currentScreen = MinecraftClient.getInstance().currentScreen;
                MinecraftClient.getInstance().setScreen(
                        new QuestionListScreen(currentScreen)
                );
            }
        });
    }


    public static void register() {
        // Register Game keybind
        openGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEYBIND_OPEN_GUI,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                KEYBIND_CATEGORY
        ));

        // Register Config keybind
        openConfig = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEYBIND_OPEN_CONFIG,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_L,
                KEYBIND_CATEGORY
        ));
    }
}