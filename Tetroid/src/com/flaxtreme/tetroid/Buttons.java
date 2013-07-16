package com.flaxtreme.tetroid;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Class for handling onscreen button logics
 * Containts array of buttons and touch-handlers
 * Note that touch-handlers mut be called from your InputProcessor
 * @author Dmitry
 * @version 4
 */
public class Buttons {

	public Array<Button> items;
	private int itouch;
	
	/**
	 * Ctor. Initialize collection
	 */
	public Buttons(){
		items = new Array<Button>(); 
	}
	
	/**
	 * Render button array
	 * @param batch SpriteBatch for rendering
	 * @param delta Delta time
	 */
	public void render(SpriteBatch batch, float delta){
		for(int i = 0; i < items.size; i++){
			items.get(i).render(batch);
		}
	}
	
	/**
	 * Add button to array
	 * @param value
	 */
	public void add(Button value){
		items.add(value);
	}
	
	/**
	 * Handle touchDown logic
	 * @param x
	 * @param y
	 * @return true if some button containts specified coords
	 */
	public boolean touchDown(int x, int y){
		for(int i = 0; i < items.size; i++)
			if (items.get(i).onHit(x, y)){
				itouch = i; return true;
			}				
		itouch = -1;
		return false;
	}
	
	/**
	 * Handle touchUp logic
	 * Try to click button that {@link #touchDown(int, int)} marked in {@link #itouch}
	 * Then loop through button and unhover them. If no click performed before and some button on hover - click it
	 * @param x
	 * @param y
	 * @return true if some button click evet raised
	 */
	public boolean touchUp(int x, int y){
		boolean clicked = false;
		// try to click marked button
		if (itouch >= 0 && itouch < items.size)
			if(items.get(itouch).onHit(x, y)){
				clicked = items.get(itouch).click();
			}
		itouch = -1;
		// unhover buttons and click on hovered if no click was made before
		for(int i = 0; i < items.size; i++){
			items.get(i).unhover(x, y, !clicked);
		}
		return false;
	}
	
	/**
	 * Handle touchDragged logic
	 * This function responds for button hovering
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean touchDragged(int x, int y){
		for(int i = 0; i < items.size; i++){
			items.get(i).hover(x, y);
		}
		return true;
	}
	
}
