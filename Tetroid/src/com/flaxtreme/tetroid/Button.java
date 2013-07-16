package com.flaxtreme.tetroid;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

/**
 * Class for game button
 * @author Dmitry
 * @version 4
 */
public class Button {

	/**
	 * Button font
	 */
	public BitmapFont font;
	/**
	 * Button sprite
	 */
	public Sprite sprite;
	/**
	 * onClick function placeholder
	 */
	public ClickDelegate onClick = null;
	/**
	 * Geometry params of button
	 */
	public int width, height, x, y;
	/**
	 * Hovered figure geometry
	 */
	public int h_width, h_height, h_x, h_y;
	
	public boolean visible, enabled;
	static float hoverK = 0.08f;
	
	private String text;	
	private float font_y;
	private float font_scale;	
	private boolean hover;
	private Color font_color;
		
	/**
	 * Default ctor for Button
	 * Creates square button with default label
	 */
	public Button(){
		this(0, 0, 100, 100, "Button");
	}
	
	/**
	 * Ctor for Button
	 * @param x x-coordinate of button left bottom point
	 * @param y y-coordinate of button left bottom point
	 * @param w width of the button
	 * @param h height of the button
	 * @param text button label
	 */
	public Button(float x, float y, float w, float h, String text){

		font = ResourceManager.getTextFont();
		font.setColor(Color.WHITE);
		
		sprite = new Sprite(ResourceManager.getButton());
		//sprite.setColor(Color.RED);
		this.x = MathUtils.ceil(x);
		this.y = MathUtils.ceil(y);
		width = MathUtils.ceil(w);
		height = MathUtils.ceil(h);
		
		h_x = MathUtils.ceil(this.x - hoverK * 0.5f * w); 
		h_y = MathUtils.ceil(this.y - hoverK * 0.5f * h);
		h_width = MathUtils.ceil((1 + hoverK) * w);
		h_height = MathUtils.ceil((1 + hoverK) * h);
		
		setText(text);
		visible = enabled = true;
		hover = false;
		font_color = new Color();
	}
	
	/**
	 * Set specified color as button texture tint. Note, that font color will set as inverse color
	 * @param color Tint color
	 */
	public void setColor(Color color){
		color.set(color.r, color.g, color.b, 0.75f);
		sprite.setColor(color);
		font_color.set(1 - color.r, 1 - color.g, 1 - color.b, 1);
	}
	
	/**
	 * Set button label and calculate font scale for fitting text in button region
	 * @param text Button label
	 */
	public void setText(String text){
		this.text = text;
		font_y = 0;
		font_scale = 1f;
		
		float to_width = width * 0.8f;
		float to_height = height * 0.5f;
		TextBounds bounds = new TextBounds();
		do {
			font.setScale(font_scale);
			font.getBounds(text, bounds);
			
			if (bounds.width <= to_width && bounds.height <= to_height)
				break;
				
			font_scale -= 0.01f;
		} while (font_scale > 0.1f);
				
		// font.getCapHeight() * [1.15 vs 0.75]
		//float K = - ( font.getAscent() + font.getDescent());
		float K = font.getCapHeight() * 1.4f;
		font_y = y + K + height / 2f;		
	}
		
	/**
	 * Render button with specified SpriteBath
	 * @param batch Rendering SpriteBatch
	 */
	public void render(SpriteBatch batch) {
		if (!visible) return;
		if (hover){
			font.setColor(font_color);
			sprite.setPosition(h_x, h_y);
			sprite.setSize(h_width, h_height);
		} else {		
			sprite.setPosition(x, y);
			sprite.setSize(width, height);
		}
		sprite.draw(batch);
		
		
		font.setScale(font_scale);
		font.drawWrapped(batch, text, x, font_y, width, HAlignment.CENTER);
		font.setColor(Color.WHITE);
	}

	/**
	 * Check if button region contains point(x, y)
	 * @param x 
	 * @param y
	 * @return true, if (x, y) in button region
	 */
	public boolean onHit(int x, int y) {
		if (x >= this.x && y >= this.y)
			if (x <= this.x + width && y <= this.y + height)
				return true;
		
		return false;
	}
	
	/**
	 * Setting hovering flag for button if coords in button region
	 * @param x
	 * @param y
	 */
	public void hover(int x, int y){
		hover = onHit(x, y);		
	}
	
	/**
	 * Disable hovering for button. If is already hovered and coordinates in button region - perform click
	 * @param x
	 * @param y
	 */
	public void unhover(int x, int y, boolean doClick){
		if (hover && doClick && onHit(x, y))
			this.click();
		hover = false;
	}
	
	/**
	 * Perform click - lauch {@link #onClick} if setted
	 * @return true if click event performed
	 */
	public boolean click() {
		if (visible && enabled && onClick != null){
			onClick.click();
			return true;
		}
		else return false;
	}

}
