package com.flaxtreme.tetroid;

public interface IActionResolver {

	public void dispose();
	
	// flurry
	public void flurry(FlurryMode flurryMode, double value);
	// share
	public void share(int score);
}
