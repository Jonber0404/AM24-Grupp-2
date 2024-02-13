package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.JumpyBirb;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		// Ställ in storleken på fönstret
		config.setWindowedMode(1280, 720); // Sätt fönsterstorleken till 1920x1080 pixlar

		config.setResizable(false); // Om du vill göra fönstret icke-ändringsbart
		config.setForegroundFPS(60); // Sätt FPS-gränsen för applikationen
		config.setTitle("JumpyBirb"); // Sätt titeln på fönstret
		new Lwjgl3Application(new JumpyBirb(), config);
	}
}

