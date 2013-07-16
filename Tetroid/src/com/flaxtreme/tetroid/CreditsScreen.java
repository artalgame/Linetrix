package com.flaxtreme.tetroid;

import java.util.Dictionary;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/*public class CreditsScreen extends BaseScreen {

	private final String spoiler = "-= Linetrix =-\nby Flaxtreme\n\nartalgame\nd0s\nlibGDX\n\nMinsk 2013\nflaxtreme com";
	BitmapFont font;
	float x, y, w, h, sh, sw, star_w;
	float time = 0;
	float maxTime = 0.01f;
	final float y_step = 3f;
	PerspectiveCamera cam;
	Sprite overlay, star;
	SpriteBatch cam_batch;
	
	public CreditsScreen(){
		super();
				
		font = ResourceManager.getFancyFont();
		font.setColor(Color.WHITE);
		
		sw = Gdx.graphics.getWidth();
		sh = Gdx.graphics.getHeight();		
		y = 0;
		w = sw * 0.85f;
		x = sw * 0.05f;
		float scale = 3f;
		TextBounds tb;
		do {
			font.setScale(scale);
			tb = font.getMultiLineBounds(spoiler);
			x = (sw - tb.width) / 2f;
			if (tb.height <= sh && tb.width <= w)
				break;
			scale -= 0.05;
		} while (scale > 0.1f);
		h = tb.height * 2;
		//Gdx.app.log("tb", "width: " + tb.width + " height: " + tb.height + " sc:" + sw + " " + sh );
		
		Texture overlay_tx = new Texture(Gdx.files.internal("data/crawl.png"));
		overlay = new Sprite(overlay_tx);
		overlay.setPosition(-(sw * 2.5f), 0);
		overlay.setSize(sw * 5, sh * 2);
		overlay.setColor(Color.BLACK);
		star = new Sprite(new TextureRegion(overlay_tx, 0, 0, 16, 16));
		
		cam_batch = new SpriteBatch();
		
		cam = new PerspectiveCamera(70, sw, sh);
		cam.position.set(sw / 2f, 0.15f * sh, sh);
		cam.direction.set(0, 9, -20);
		cam.near = 1;
		cam.far = sh * 20;
		cam.update(false);
				
	}
	
	@Override
	public void show() {
		super.show();
		
		Gdx.input.vibrate(new long[]{250, 100, 100, 100, 100, 100, 500, 100, 100, 100, 250, 250, 2000}, 0);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);		
		
		cam_batch.setProjectionMatrix(cam.combined);		
		cam_batch.begin();
		font.drawMultiLine(cam_batch, spoiler, x, y, w, HAlignment.CENTER);		
		overlay.draw(cam_batch);
		cam_batch.end();
		
		
		
		time += delta;
		if (time >= maxTime){
			time = 0;
			y += y_step;
			if (y > sh + h)
				y = 0;		
			batch.begin();
			for(int i = 0; i < 50; i++){
				star_w = MathUtils.random(1, 4);
				star.setSize(star_w, star_w);			
				star.setPosition(MathUtils.random(star_w, sw - star_w), MathUtils.random(star_w, sh * 2));
				star.draw(batch);
			}
			batch.end();
		}
	}
	
	@Override
	public void onBackKey() {
		Gdx.input.cancelVibrate();
		GameApp.get().setScreen(new MenuScreen());
	}*/
	/*
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.LEFT)
			camera.near += 0.01f;
		if (keycode == Keys.RIGHT)
			camera.near -= 0.01f;
		if (keycode == Keys.UP)
			camera.far += 0.1f;
		if (keycode == Keys.DOWN)
			camera.far -= 0.1f;
		//camera.lookAt(2, 0, 0);
		camera.update(true);
		batch.setProjectionMatrix(camera.combined);
		return super.keyDown(keycode);
	}*/
//}
public class CreditsScreen extends BaseScreen{
	private String screenTitle;
	private String siteLink = "Flaxtreme.com";
	private String copyRightString = "©Flaxtreme 2013";
	private HashMap <String, String> roles;
	
	private BitmapFont titleFont;
	private BitmapFont linkFont;
	private BitmapFont roleKeyFont;
	private BitmapFont roleValueFont;
	private BitmapFont copyRightFont;
	
	private BackgroundGradient backgroundGradient;
	
	
	private final String spoiler = "-= Linetrix =-\nby Flaxtreme\n\nartalgame\nd0s\nlibGDX\n\nMinsk 2013\nflaxtreme com";
	
	BitmapFont font;
	float x, y, w, h, sh, sw, star_w;
	float time = 0;
	float maxTime = 0.01f;
	final float y_step = 3f;
	PerspectiveCamera cam;
	Sprite overlay, star;
	SpriteBatch cam_batch;
	
	public CreditsScreen(){
		super();
		//Start Alex's code
		LanguageManager lang = GameApp.get().lang;
		screenTitle = lang.get("Tetlines");
		loadRoles();
		loadFonts();
		backgroundGradient = new BackgroundGradient();
		//Finish Alex's code
	}
	
	private void loadFonts() {
		titleFont = ResourceManager.getTextFont();
		titleFont.setColor(Color.WHITE);
		titleFont.setScale(1);
		
		linkFont = ResourceManager.getTextFont();
		linkFont.setColor(Color.BLUE);
		linkFont.setScale(0.5f);
		
		roleKeyFont = ResourceManager.getTextFont();
		roleKeyFont.setColor(Color.ORANGE);
		roleKeyFont.setScale(0.7f);
		
		roleValueFont = ResourceManager.getTextFont();
		roleValueFont.setColor(Color.WHITE);
		roleValueFont.setScale(0.7f);
		
		copyRightFont = ResourceManager.getTextFont();
		copyRightFont.setColor(Color.WHITE);
		copyRightFont.setScale(0.3f);
	}

	private void loadRoles() {
		LanguageManager lang = GameApp.get().lang;
		String roleKey = "roleKey";
		String roleValue = "roleValue";
		
		roles = new HashMap<String, String>();
		int i=0;
		while(true){
			String currentRoleKey = roleKey + i;
			String currentRoleValue = roleValue + i;
			if(lang.get(currentRoleKey) != currentRoleKey){
				String roleName = lang.get(currentRoleKey);
				String value = lang.get(currentRoleValue);
				roles.put(roleName, value);
				i++;
			}
			else	break;
		}
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
		
		backgroundGradient.draw(batch, delta);
		renderScreen(batch, delta);
		
		batch.end();		

	}
	
	private void renderScreen(SpriteBatch batch, float delta) {
		float h = Gdx.graphics.getHeight();
		float w = Gdx.graphics.getWidth();
		float x = (Gdx.graphics.getWidth()) / 2f;
		float y = h;
		float ygap = 1.5f;

		titleFont.draw(batch, screenTitle,x-50, y);
		titleFont.setColor(Color.WHITE);
		
		for(int i=0; i<roles.size();i++)
		{
			roleKeyFont.draw(batch, roles.keySet().toArray(new String[0])[i], x - 150, y - i * 50 - 50);
			roleKeyFont.setColor(Color.ORANGE);
			roleKeyFont.setScale(0.3f);
			
			roleValueFont.draw(batch, roles.values().toArray(new String[0])[i], x-30, y - i * 50 - 50);
			roleValueFont.setColor(Color.WHITE);
			roleValueFont.setScale(0.3f);
		}
	}

	@Override
	public void onBackKey() {
		GameApp.get().setScreen(new MenuScreen());
	}
	/*@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.LEFT)
			camera.near += 0.01f;
		if (keycode == Keys.RIGHT)
			camera.near -= 0.01f;
		if (keycode == Keys.UP)
			camera.far += 0.1f;
		if (keycode == Keys.DOWN)
			camera.far -= 0.1f;
		//camera.lookAt(2, 0, 0);
		camera.update(true);
		batch.setProjectionMatrix(camera.combined);
		return super.keyDown(keycode);
	}*/
}
