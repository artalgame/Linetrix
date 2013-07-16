package com.flaxtreme.tetroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.flaxtreme.tetroid.Utils.HSL;

public class BackgroundGradient {

	Sprite bg;
	HSL hsl;
	Color bg_color = new Color(Color.RED);
	boolean hsl_up = true;
	float hsl_step = 0.5f;
	float time;
	float timeDelay = 0.2f;
		
	
	public BackgroundGradient(){
		time = 100;
		bg = ResourceManager.getGradient();
		bg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		hsl = new HSL(MathUtils.random(0, 360), 0.75f, 0.5f);
	}
		
	public void draw(SpriteBatch batch, float delta){
		time += delta;
		if (time > timeDelay){
			time = 0;
			if (hsl_up){
				hsl.h += hsl_step;
				if (hsl.h >= 360-hsl_step)
					hsl_up = false;
			} else {
				hsl.h -= hsl_step;
				if (hsl.h < hsl_step)
					hsl_up = true;
			}	
			Utils.HSL2RGB(hsl, bg_color);
		}
		
		bg.setColor(bg_color);
		bg.draw(batch);
	}
	
}
