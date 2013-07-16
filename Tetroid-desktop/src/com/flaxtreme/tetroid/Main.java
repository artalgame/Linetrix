package com.flaxtreme.tetroid;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Tetroid";
		cfg.useGL20 = true;
		cfg.width = 320; cfg.height = 480;
		//cfg.width = 480; cfg.height = 854;
		//cfg.width = 240; cfg.height = 320;
		
		new LwjglApplication(new GameApp(new ActionResolver()), cfg);
		//new LwjglApplication(new Crawl(), cfg);
	}
}
