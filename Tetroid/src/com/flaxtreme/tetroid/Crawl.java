package com.flaxtreme.tetroid;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Crawl implements ApplicationListener {

	float sw, sh, w, h, x, y;
	String text = "EPISODE IV\n A NEW HOPE\n\nIt is a period of civil war.\nRebel spaceships, striking from a hidden base, have won their first victory against the evil Galactic Empire.";
	BitmapFont font;
	PerspectiveCamera camera;
	SpriteBatch batch;
		
	@Override
	public void create() {
		// font
		font = new BitmapFont();
		font.setColor(Color.YELLOW);
		// sizes
		sw = Gdx.graphics.getWidth();
		sh = Gdx.graphics.getHeight();		
		w = sw * 0.75f;
		x = (sw - w) / 2f;
		y = sh;
		// batch
		batch = new SpriteBatch();		
		// camera
		camera  = new PerspectiveCamera(65, sw, sh);
		
		camera.position.set(sw / 2f, sh , sh);
		camera.direction.set(0, 10, -20);
		camera.near = 1;
		camera.far = sh * 20;
		camera.update(false);
		
	}
	
	float k = 0.1f;
	
	void checkTouch(){
		if (Gdx.input.isKeyPressed(Keys.UP)){
			camera.direction.add(0, k, 0);
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)){
			camera.direction.sub(0, k, 0);
		} else if (Gdx.input.isKeyPressed(Keys.LEFT)){
			camera.direction.sub(k, 0, 0);
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)){
			camera.direction.add(k, 0, 0);
		} else if (Gdx.input.isKeyPressed(Keys.Z)){
			camera.direction.sub(0, 0, k);
		} else if (Gdx.input.isKeyPressed(Keys.X)){
			camera.direction.add(0, 0, k);
		} else if (Gdx.input.isKeyPressed(Keys.SPACE)){
			Gdx.app.log("direction", String.format("%.2f %.2f %.2f", camera.direction.x, camera.direction.y, camera.direction.z));
			Gdx.app.log("position", String.format("%.2f %.2f %.2f", camera.position.x, camera.position.y, camera.position.z));
		}
		camera.normalizeUp();
		camera.update();
	}
	
	float maxTime = 0.05f;
	float time = 0;
			
	void movement(){
		time += Gdx.graphics.getDeltaTime();
		if (time < maxTime) return;
		time = 0;
		
		camera.position.sub(0, 1, 0);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		
		checkTouch();
		movement();
		
		batch.setProjectionMatrix(camera.combined);		
		batch.begin();
		//
		font.drawWrapped(batch, text, x, y, w, HAlignment.LEFT);	
		//
		batch.end();
	}
	

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}

	
}
