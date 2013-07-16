package com.flaxtreme.tetroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SplashScreen extends BaseScreen {

	float sw, sh;
	Sprite bg_color;
	Sprite animation;
	Texture texture;
	Animation walkAnimation;
	TextureRegion[] walkFrames;
	TextureRegion currentFrame;
	Color color;

	float stateTime;
	float allTime;
	float fx, fy, fw, fh;
	boolean init = false;
	final String textureFile = "data/splash.png";
	AssetManager assertManager;

	static int FRAME_COLS = 1;
	static int FRAME_ROWS = 21;
	static float PER_FRAME = 0.1f;
	static float displayTime = 2.50f;
	static float overallTime = 3.00f;
	static float animMaxTime = FRAME_COLS * FRAME_ROWS * PER_FRAME;

	public SplashScreen(){
		super();
		
		assertManager = new AssetManager();
		assertManager.load(textureFile, Texture.class);
		//assertManager.finishLoading();
	}
	
	
	@Override
	public void show() {
		super.show();
		
	}
	
	public void initialize(){
		sw = Gdx.graphics.getWidth();
		sh = Gdx.graphics.getHeight();

		texture = assertManager.get(textureFile, Texture.class);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bg_color = new Sprite(new TextureRegion(texture, 0, 0, 10, 10));
		bg_color.setSize(sw, sh);

		float frame_w = 480f / (float) FRAME_COLS;
		float frame_h = 2048f / (float) FRAME_ROWS;

		TextureRegion[][] tmp = TextureRegion.split(texture, (int) frame_w, (int) frame_h);
		walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}
		walkAnimation = new Animation(PER_FRAME, walkFrames);
		stateTime = 0f;
		allTime = 0;

		fw = frame_w;
		fh = frame_h;

		if (sw < frame_w) {
			fh = sw * frame_h / frame_w;
			fw = sw;
		}

		fx = (sw - fw) / 2f;
		fy = sh - (sh - fh * 0.5f) / 2f;

	}

	@Override
	public void hide() {
		super.hide();

		texture.dispose();
		assertManager.dispose();
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);

		if (!init){
			if (!assertManager.update()) return;
			initialize();
			init = true;
		}
		
		stateTime += delta;
		if (stateTime < animMaxTime) {
			currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		} else {
			currentFrame = walkAnimation.getKeyFrame(animMaxTime, true);
		}
		allTime += delta;

		batch.begin();
		if (allTime < displayTime) {
			// anim
			bg_color.draw(batch);
			batch.draw(currentFrame, fx, fy, fw, fh);
		} else {
			// fade out
			bg_color.setColor(Utils.opacity(Color.WHITE, (overallTime - allTime) / (overallTime - displayTime) ));
			bg_color.draw(batch);
		}
		batch.end();
		
			// switch
		if (Gdx.input.justTouched() ||  allTime > overallTime){
			GameApp.get().setScreen(new MenuScreen());
		}
		
	}

}
