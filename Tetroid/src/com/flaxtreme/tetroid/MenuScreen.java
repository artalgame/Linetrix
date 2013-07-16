package com.flaxtreme.tetroid;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.flaxtreme.tetroid.GameScreen.BoardTypes;

/**
 * Menu screen class
 * @author	DimS
 * @version 0.3
 */

public class MenuScreen extends BaseScreen {

	Buttons buttons;
	BackgroundGradient bg;
	
	public MenuScreen(){
		super();
		
		buttons = new Buttons();
		addButtons();
		
		bg = new BackgroundGradient();		
	}
	
	public void addButtons(){
		// зофтвер намагнитович шпиндель!
		Button b;
		float sh = Gdx.graphics.getHeight();
		
		float w = Gdx.graphics.getWidth() * 0.65f;
		float h = sh * 0.12f;
		float x = (Gdx.graphics.getWidth() - w) / 2f;
		float y = 2.5f * h;
		float ygap = 1.5f;
		
		// lang
		LanguageManager lang = GameApp.get().lang;
		
		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(Color.RED); colors.add(Color.GREEN); colors.add(Color.BLUE);  colors.add(Color.YELLOW);
		Collections.shuffle(colors);
		int color_index = 0;
		
		b = new Button(x, sh-y, w, h, lang.get("Tetlines"));
		b.onClick = new ClickDelegate() {			
			@Override
			public void click() {
				GameScreen gs = new GameScreen(BoardTypes.THREESOME);
				GameApp.get().setScreen(gs);
			}
		};
		b.setColor(colors.get(color_index++));
		buttons.add(b);
				
		y += ygap * h;
		b = new Button(x, sh-y, w, h, lang.get("Tetrix"));
		b.onClick = new ClickDelegate() {			
			@Override
			public void click() {
				GameScreen gs = new GameScreen(BoardTypes.CLASSIC);
				GameApp.get().setScreen(gs);
			}
		};
		b.setColor(colors.get(color_index++));
		buttons.add(b);
		
		y += ygap * h;
		b = new Button(x, sh-y, w, h, lang.get("Credits"));
		b.onClick = new ClickDelegate() {			
			@Override
			public void click() {
				GameApp.get().setScreen(new CreditsScreen());
			}
		};
		b.setColor(colors.get(color_index++));
		buttons.add(b);

		y += ygap * h;
		b = new Button(x, sh-y, w, h, lang.get("Exit"));
		b.onClick = new ClickDelegate() {			
			@Override
			public void click() {
				((MenuScreen)MenuScreen.get()).onExit();
			}
		};
		b.setColor(colors.get(color_index++));
		buttons.add(b);
	}
	
	@Override
	public void onBackKey() {
		onExit();
	}
		
	public void onExit(){
		Gdx.app.exit();
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
	
	@Override
	public void show() {
		super.show();
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
			
		batch.enableBlending();
		batch.begin();
		
		bg.draw(batch, delta);
		buttons.render(batch, delta);
		
		batch.end();		
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		buttons.touchDown(screenX, Gdx.graphics.getHeight() - screenY);
		
		return super.touchDown(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		buttons.touchUp(screenX, Gdx.graphics.getHeight() - screenY);
		
		return super.touchUp(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		buttons.touchDragged(screenX, Gdx.graphics.getHeight() - screenY);
		
		return super.touchDragged(screenX, screenY, pointer);
	}
}
