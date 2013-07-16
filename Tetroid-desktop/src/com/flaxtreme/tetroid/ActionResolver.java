package com.flaxtreme.tetroid;

import com.badlogic.gdx.Gdx;


public class ActionResolver implements IActionResolver {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flurry(FlurryMode flurryMode, double value) {
		//Gdx.app.log("Flury", flurryMode.name().toString());
	}

	@Override
	public void share(int score) {
		Gdx.app.log("Share", "My score: " + score);
	}

}
