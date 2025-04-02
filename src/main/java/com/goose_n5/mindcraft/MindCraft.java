package com.goose_n5.mindcraft;

import com.goose_n5.mindcraft.event.KeybindManager;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.Key;

public class MindCraft implements ModInitializer {
	public static final String MOD_ID = "mindcraft";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final String CONFIG_DIR = "./config/mindcraft/";

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		File configDir = new File(CONFIG_DIR);
		if (!configDir.exists()) {
			configDir.mkdirs();
			System.out.println("Created config directory");
		}

		File questionsFile = new File(CONFIG_DIR,"questions.json");
		if (!questionsFile.exists()) {
			try (InputStream inputStream = MindCraft.class.getResourceAsStream("/assets/mindcraft/questions.json")) {
				Files.copy(inputStream, questionsFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		KeybindManager.register();
		KeybindManager.registerKeyInputs();

		LOGGER.info("Hello Fabric world!");
	}
}