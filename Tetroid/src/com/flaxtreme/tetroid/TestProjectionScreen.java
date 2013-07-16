package com.flaxtreme.tetroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.math.Matrix4;

public class TestProjectionScreen extends BaseScreen {

	float sw, sh, w, h, x, y;
	Matrix4 matrix;
	String text = "EPISODE IV\n A NEW HOPE\n\nIt is a period of civil war. Rebel spaceships, striking from a hidden base, have won their first victory against the evil Galactic Empire. During the battle, Rebel spies managed to steal secret plans to the Empire’s ultimate weapon, the DEATH STAR, an armored space station with enough power to destroy an entire planet. Pursued by the Empire’s sinister agents, Princess Leia races home aboard her starship, custodian of the stolen plans that can save her people and restore freedom to the galaxy….";
	BitmapFont font;
	
	@Override
	public void show() {
		super.show();
		//		
		font = new BitmapFont();
		font.setColor(Color.YELLOW);
		
		sw = Gdx.graphics.getWidth();
		sh = Gdx.graphics.getHeight();
		
		w = sw * 0.5f;
		x = (sw - w) / 2f;
		y = sh * 0.1f;
		
		// set camera
		camera = new OrthographicCamera(sw, sh);
		
		//camera.position.set(5, 5, 1);
		//camera.direction.set(0, 0, 1);
		//camera.near = 1;
		//camera.far = 1000;
		camera.update(false);
		
	}
	
	float k = 0;
	float dk = 0.05f;
	
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.UP:
			camera.direction.add(0, dk, 0);
			break;
		case Keys.DOWN:
			camera.direction.sub(0, dk, 0);
			break;
		case Keys.LEFT:
			camera.direction.sub(dk, 0, 0);
			break;
		case Keys.RIGHT:
			camera.direction.add(dk, 0, 0);
			break;

		default:
			break;
		}
		
		
		camera.update();
		
		return super.keyDown(keycode);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		//
		batch.setProjectionMatrix(camera.combined);
		//batch.setTransformMatrix(matrix);
		
		batch.begin();
		
		font.drawMultiLine(batch, text, x, y, w, HAlignment.LEFT);
		
		batch.end();
		//
	}
	
}
