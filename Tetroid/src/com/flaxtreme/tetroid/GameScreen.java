package com.flaxtreme.tetroid;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

/**
 * Screen that contain game board, game menu and handles figure movements
 * @author Dmitry
 * @version 4
 */
public class GameScreen extends BaseScreen {

	public enum BoardTypes {
		CLASSIC, THREESOME
	};

	public enum GameState {
		PLAY, END, PAUSE
	};

	BoardClassic board;
	GameState state;
	float sw, sh, tw, tg, bx, by, bw, bh;
	float txty, txtx, txtw;
	float pts_scale;
	Sprite tile, bg_tile;
	BackgroundGradient bg;
	BitmapFont font;
	Buttons buttons;

	int startX, startY;
	float deltaTouchX, deltaTouchY;
	float touchTime;
	boolean isTouchDown;
	int catchedFigure;

	public GameScreen(BoardTypes boardType) {
		super();

		setBoard(boardType);
		tile = ResourceManager.getFigure();
		bg_tile = ResourceManager.getTile();
		font = ResourceManager.getTextFont();
		
		bg = new BackgroundGradient();
		
		GameApp.get().actions.flurry(FlurryMode.GAME_START, 0);
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
		
	@Override
	public void pause() {
		if (state == GameState.PLAY)
			setState(GameState.PAUSE);
			
		super.pause();
	}

	public void togglePause() {
		if (state == GameState.PLAY)
			setState(GameState.PAUSE);
		else if (state == GameState.PAUSE)
			setState(GameState.PLAY);
	}

	public void setState(GameState state) {		
		switch (state) {
		case PLAY:
			buttons.items.get(0).setText(GameApp.get().lang.get("Continue"));
			board.setActive(true);
			break;
		case PAUSE:
			buttons.items.get(0).setText(GameApp.get().lang.get("Continue"));
			board.setActive(false);
			break;
		case END:
			board.setActive(false);
			buttons.items.get(0).setText(MathUtils.ceil(board.score) + " pts");
			Gdx.input.vibrate(new long[]{100, 100, 100, 50, 100, 25}, -1);
			GameApp.get().actions.flurry(FlurryMode.GAME_OVER, 0);
			break;
		}
		
		this.state = state;
	}

	/**
	 * Init buttons and their colors, sizes and top-bar 
	 */
	public void initMenu() {
		// calc sizes
		float w = sw * 0.63f;
		float h = sh * 0.12f;
		float x = (sw - w) / 2f;
		float y = 2 * h;

		// lang manager
		LanguageManager lang = GameApp.get().lang;
		
		// init buttons
		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(Color.RED); colors.add(Color.GREEN); colors.add(Color.BLUE);  colors.add(Color.YELLOW);
		Collections.shuffle(colors);
		int color_index = 0;
		
		buttons = new Buttons();
		Button b;
		
		/*
		 * First button
		 * Handle continue when game playing
		 * and share when game end 
		 */
		b = new Button(x, sh - y, w, h,  lang.get("Continue"));
		b.onClick = new ClickDelegate(this) {
			@Override
			public void click() {
				if (((GameScreen) screen).state != GameState.END){
					((GameScreen) screen).setState(GameState.PLAY);
				} else {
					GameApp.get().actions.share(MathUtils.ceil(((GameScreen) screen).board.score));
				}				
			}
		};
		b.setColor(colors.get(color_index++));
		buttons.add(b);


		y += 1.5f * h;
		b = new Button(x, sh - y, w, h, lang.get("Restart"));
		b.onClick = new ClickDelegate(this) {
			@Override
			public void click() {
				((GameScreen) screen).restart();
			}
		};
		b.setColor(colors.get(color_index++));
		buttons.add(b);

		y += 1.5f * h;
		b = new Button(x, sh - y, w, h, lang.get("Menu"));
		b.onClick = new ClickDelegate() {
			@Override
			public void click() {
				GameApp.get().setScreen(new MenuScreen());
			}
		};
		b.setColor(colors.get(color_index++));
		buttons.add(b);
		
		// init score font
		pts_scale = 0.5f;
		float to_height = sh * 0.05f;
		TextBounds tb;
		do {
			font.setScale(pts_scale); 
			pts_scale -= 0.005f;
			tb = font.getBounds("pts 159321");
			if (tb.height <= to_height)
				break;			
		} while(pts_scale > 0.1f);
		
		// init score pos
		txty = sh - font.getAscent();
		txtx = sw * 0.5f;
		txtw = sw * 0.45f;
	}

	@Override
	public void show() {
		super.show();
		// get screen info
		sw = Gdx.graphics.getWidth();
		sh = Gdx.graphics.getHeight();

		calcBoardSize();

		initMenu();

		// start
		restart();
	}

	/**
	 * Calculate board size and position
	 */
	public void calcBoardSize() {
		// tile is square, so tile height == tile width
		
		bw = sw * 0.95f; // width = % of screen
		bh = sh * 0.95f; // max border height
		tw = bw / (float) board.getWidth(); // tile width		
		
		float bh_fit = tw * board.getHeight();		
		// check board max height
		// if it greater than screen height - calc new tw and bw
		if (bh_fit > bh){						
			tw = bh / (float) board.getHeight();
			bw = tw * board.getWidth();
		} else {
			bh = tw * board.getHeight();
		}
		
		tg = 0; // tw * 0.02f; // the gap between tiles
		// left board border, board centered horizontally
		bx = (sw - bw) / 2f;
		// top board border, board centered vertically
		by = (sh - (tw + tg) * board.getHeight()) / 2f;

		// inits a tile
		tile.setSize(tw, tw);
		bg_tile.setSize(tw, tw);
		// set opasity
		Color c = bg_tile.getColor();
		c.set(c.r, c.g, c.b, 0.2f);
		bg_tile.setColor(c);
				
		// inits finger-related vars
		deltaTouchX = Gdx.graphics.getWidth() / (board.getWidth() + 2);
		deltaTouchY = Gdx.graphics.getHeight() / (board.getHeight() + 4);
	}

	/**
	 * Restart the board's game
	 */
	public void restart() {
		board.setActive(false);
		board.start();
		state = GameState.PLAY;
	}

	/**
	 * Timer
	 * @param delta
	 */
	public void tick(float delta) {
		board.tick(delta);
		if (board.finished && state != GameState.END) {
			setState(GameState.END);
		}
	}

	@Override
	public void render(float delta) {
		tick(delta);
		super.render(delta);

		batch.begin();

		// render bg
		bg.draw(batch, delta);
		
		int i, j;
		// rendering bg tiles
		for (i = 0; i < board.getWidth(); i++) {
			for (j = 0; j < board.getHeight(); j++) {
				bg_tile.setPosition(bx + (tw + tg) * i, (by + (tw + tg) * j));
				bg_tile.draw(batch);
			}
		}
		// rendering board
		for (i = 0; i < board.getWidth(); i++) {
			for (j = 0; j < board.getHeight(); j++) {
				if (board.map[i][j] == 0)
					continue;
				tile.setPosition(bx + (tw + tg) * i, (by + (tw + tg) * j));
				tile.setColor(board.colors[board.map[i][j]]);
				
				if (board.mfx[i][j] != 0){
					tile.setColor(Utils.opacity(tile.getColor(), board.mfx[i][j] * 0.75f + 0.25f));
				}
				tile.draw(batch);
			}
		}
		// rendering figure
		if (board.figure.visible)
		for (i = 0; i < board.figure.bounds; i++) {
			for (j = 0; j < board.figure.bounds; j++) {
				if (board.figure.map[i][j] == 0)
					continue;
				tile.setPosition(bx + (tw + tg) * (i + board.figure.x),
						(by + (tw + tg) * (j + board.figure.y)));
				tile.setColor(board.colors[board.figure.getColorAt(i, j)]);
				if (board.finished){
					tile.setColor(Color.WHITE);
				}
				tile.draw(batch);
			}
		}
		// render text
		renderText();

		if (state == GameState.PAUSE) {
			renderPause(delta);
		} else if (state == GameState.END) {
			renderGameover(delta);
		}
		batch.end();

	}

	private void renderText(){
		font.setScale(pts_scale);
		font.drawWrapped(batch, String.format("pts %.0f", board.score), txtx, txty, txtw, HAlignment.RIGHT);
	}
	
	private void renderPause(float delta) {
		buttons.render(batch, delta);
	}

	private void renderGameover(float delta) {
		buttons.render(batch, delta);
	}

	/**
	 * Set game board type
	 * 
	 * @param boardType
	 *            Type of board
	 */
	public void setBoard(BoardTypes boardType) {
		switch (boardType) {
		case THREESOME:
			GameApp.get().actions.flurry(FlurryMode.GAME_LINES, 0);
			board = new BoardLines();
			break;
		case CLASSIC:
		default:
			GameApp.get().actions.flurry(FlurryMode.GAME_CLASSIC, 0);
			board = new BoardClassic();
			break;
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.LEFT:
			board.moveFigure(-1, 0);
			break;
		case Keys.RIGHT:
			board.moveFigure(1, 0);
			break;
		case Keys.UP:
			board.rotateFigure(true);
			//board.moveFigure(0, 1);
			break;
		case Keys.U: board.moveFigure(0, 1); break;
		case Keys.DOWN:
			board.moveFigure(0, -1);
			break;
		// case Keys.R: board.rotateFigure(true); break;
		case Keys.SPACE:
			while (board.moveFigure(0, -1))
				;
			break;
		case Keys.BACK:
		case Keys.ESCAPE:
			togglePause();
			break;
		default:
			return super.keyDown(keycode);
		}
		return true;
	}

	/**
	 * Input handler region
	 */

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (state == GameState.PLAY) {
			// on-game touch
			if (pointer + button != 0) return false;		// don't allow multitouch
			startX = screenX;
			startY = screenY;
			
			isTouchDown = true;
			catchedFigure = board.figure.fid;
			//Gdx.app.log("touch down", "id: " + board.figure.uid);
			
			return true;
		} else {
			// menu touch
			buttons.touchDown(screenX, Gdx.graphics.getHeight() - screenY);
		}

		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (state == GameState.PLAY) {
			// on-game
			if (pointer + button != 0) return false;		// don't allow multitouch
			if (!isTouchDown) return false;					// if figure moved
			if (catchedFigure != board.figure.fid) return false;// if new figure
						
			// try move down
			float dy = screenY - this.startY;
			float fstep = dy / this.deltaTouchY;
			if (fstep > 1.5f){
				while (this.board.moveFigure(0, -1));
				return true;
			}
			
			// else - rotate
			this.board.rotateFigure(true);
			return true;
		} else {
			// menu touch
			buttons.touchUp(screenX, Gdx.graphics.getHeight() - screenY);
		}

		return super.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (state == GameState.PLAY) {
			// sticky-finger movement
			if (pointer != 0) return false;							// don't allow multitouch
									
			float dx = screenX - startX;
			float dy = screenY - startY;
			float step; boolean negative;
			
			final boolean axisX = !(dy - 2.99f * Math.abs(dx) > 0); 
						
			if (axisX){
				// horizontal			
				negative = (dx < 0);
				if (negative)
					dx *= (-1);
				
				step = MathUtils.floor(dx / this.deltaTouchX);
				if (step != 0){
					isTouchDown = false;
					startX = screenX;
					while(step-- > 0)
						this.board.moveFigure((negative) ? -1 : 1, 0);
					return true;
				}
			} else {
				// vertical
				if (catchedFigure != board.figure.fid) return false;	// movement not spreads for next figure
				
				step = dy / this.deltaTouchY;
				if (step > 0.5){
					//Gdx.app.log("Step down", String.format("%.2f", step));
					isTouchDown = false;
					startY = screenY;
					if (step > 3.25f)
						while(catchedFigure == board.figure.fid && board.moveFigure(0, -1));
					else
						while (catchedFigure == board.figure.fid && step-- > 0)
							board.moveFigure(0, -1);
					return true;
				}
			}
			
			return true;
		} else {
			// menu drag
			buttons.touchDragged(screenX, Gdx.graphics.getHeight() - screenY);
		}
		return super.touchDragged(screenX, screenY, pointer);
	}
	
}
