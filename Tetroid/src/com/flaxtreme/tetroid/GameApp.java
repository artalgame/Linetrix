package com.flaxtreme.tetroid;

import com.badlogic.gdx.Game;

/**
 * Game starter class
 * @author Dmitry
 * @version 0.2
 */
public class GameApp extends Game {
	
	static GameApp instance;
	static GameApp get(){
		return instance;
	}
		
	public IActionResolver actions;
	//public ResourceManager res;
	public LanguageManager lang;
	
	public GameApp(IActionResolver actionResolver){
		instance = this;
		actions = actionResolver;
	}
	
	@Override
	public void create() {
		actions.flurry(FlurryMode.START, 0);
		
		//res = new ResourceManager();
		ResourceManager.load();
		lang = new LanguageManager();
		
		//setScreen(new CreditsScreen());
		setScreen(new SplashScreen());
		//setScreen(new GameScreen(BoardTypes.CLASSIC));
	}
		
	@Override
	public void dispose() {
		actions.flurry(FlurryMode.STOP, 0);
		ResourceManager.unload();
		
		super.dispose();
	}
	
}
