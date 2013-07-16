package com.flaxtreme.tetroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ResourceManager {
	
	public static TextureAtlas atlas;
	public static BitmapFont fontText;
	public static BitmapFont fontFancy; 
	//private static Array<Texture> disposable;
			
	public static void load(){
		//disposable = new Array<Texture>();
		
		atlas = new TextureAtlas(Gdx.files.internal("data/sprites.pack"));
		fontText = getFont("font/fundamental.72.png");
		fontText.setOwnsTexture(true);
		
		fontFancy = getFont("font/blox.png");
		fontFancy.setOwnsTexture(true);
	}
	
	public static void unload(){
		//Gdx.app.log(ResourceManager.class.getName(), "unload");
		//for(int i = 0; i < disposable.size; i++)
		//	disposable.get(i).dispose();
		//disposable.clear();
		
		atlas.dispose();
		fontText.dispose();
		fontFancy.dispose();
	}
	
	public static BitmapFont getFont(String fontName){		
		Texture ft = new Texture(Gdx.files.internal(fontName));
		ft.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		BitmapFont font = new BitmapFont(Gdx.files.internal(fontName.replace("png", "fnt")), new TextureRegion(ft), false);
		//disposable.add(ft);
		return font;
	}
		
	public static BitmapFont getTextFont(){		
		return fontText;
	}
	
	public static BitmapFont getFancyFont(){
		return fontFancy;
	}
		
	public static Sprite getTile(){
		return atlas.createSprite("bg_tile");
	}
	
	public static Sprite getGradient(){
		return atlas.createSprite("bg");
	}
	
	public static Sprite getButton(){
		return atlas.createSprite("button");
	}
	
	public static Sprite getFigure(){
		return atlas.createSprite("figure");
	}
	
}
