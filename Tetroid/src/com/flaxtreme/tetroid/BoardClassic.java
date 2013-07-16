package com.flaxtreme.tetroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

/**
 * Base class for Tetrix board logic
 * Implements classic Tetris game logic
 * 
 * TODO: Fix langing bug for figurs with origin
 * TODO: Document it all
 * 
 * @author DimS
 * @version 0.3
 */
public class BoardClassic {

	public byte map[][];
	public float mfx[][];
	public Figure figure;
	public Color[] colors;
	public boolean finished;
	public float score;
	
	/**
	 * Borads width and height in tiles unit (TU)
	 */
	protected int map_w;
	protected int map_h;
	/**
	 * Flag for board state
	 */
	protected boolean active;
	
	public float delayTime;
	public float moveTime;
	public float delayTimeDelta = 0.0085f;	
	public float delayTimeMin = 0.15f;
	public float cleanerTime = 0;
	public float cleanerTimeMax = 0.005f;
	
	public float mfx_step = 0.02f; 
	
	/**
	 * Board default constructor
	 */
	public BoardClassic() {
		this(10, 15);
	}
	
	/**
	 * Parametrized constructor 
	 * @param width Width of new board
	 * @param height Height of new board
	 */
	public BoardClassic(int width, int height){
		colors = new Color[]{ Color.WHITE,
				Color.RED, Color.GREEN, Color.BLUE,
				Color.YELLOW, Color.CYAN
		};
		Figure.maxFigure = 7;
		Figure.multiColor = false;
		
		figure = new Figure(colors.length);
		setActive(false);
		setSize(width, height);
	}
	
	/**
	 * Set board width and height in TU
	 * This method inits {@link #map}, so must be called right after constructor
	 * @param width
	 * @param height
	 */
	public void setSize(int width, int height){
		map_w = width;
		map_h = height;
		map = new byte[map_w][map_h];
		mfx = new float[map_w][map_h];
		
		this.clear();
	}
		
	/**
	 * Setter for {@link #active}
	 * @param state State to set
	 */
	public void setActive(boolean state){
		active = state;
		moveTime = 0;
		if (state){
			finished = false;
		}
	}
	/**
	 * Getter for active
	 * @return State
	 */
	public boolean getActive(){
		return active;
	}
	/**
	 * Getter for width
	 * @return Board width
	 */
	public int getWidth(){
		return map_w;
	}
	/**
	 * Getter for height
	 * @return Board height
	 */
	public int getHeight(){
		return map_h;
	}

	/**
	 * Starts board game
	 */
	public void start(){
		clear();
		delayTime = 1f;
		score = 0;
		setActive(true);
		getNextFigure();
	}
	
	/**
	 * Generates new figure in centre-top of board
	 */
	public void getNextFigure(){
		figure.reborn(map_w, map_h, colors.length-1);
		moveTime = 0;
		speedUp();
	}
	
	/**
	 * Handles level difficulty variables, like {@link #delayTime} and {@link #colors}
	 */
	public void speedUp(){
		// decrease delay time
		if (delayTime > delayTimeMin)
		delayTime -= delayTimeDelta;
	}
	
	//boolean tick_lock = false;
	
	/**
	 * Perform timed actions in board
	 * Also check gameover situation
	 * @param delta Timespan in ms
	 */
	public synchronized void tick(float delta){
		if (!active) return;
		//if (tick_lock) return;
		//tick_lock = true;
		
		findArea();		
		// use delete time delay for decreaseingFPS-related bugs
		cleanerTime += delta;
		deleteArea();
		
		if (checkGameOver()){
		//	tick_lock = false;
			return;
		}
		
		moveTime += delta;
		// moving figure down on timer
		if (moveTime >= delayTime){
			moveFigure(0, -1);
			moveTime = 0;
		}
		
		//tick_lock = false;
	}
	
	/**
	 * Check is there place for new figure. If no - goto GameOver
	 * @return true in GO state
	 */
	public boolean checkGameOver(){
		if (!checkBlocked())
			return false;
		
		finished = true;
		setActive(false);	
		return true;
	}
	
	/**
	 * Searching area to delete and mark it on mfx;
	 * In classic variant: searching for full horisontal rows
	 * Must be overrided in different game modes
	 */
	public void findArea(){
		int i, j;
		
		for(j = 0; j < map_h; j++){	// for each row
			for(i = 0; i < map_w; i++)	// for each column
				if (map[i][j] == 0 || mfx[i][j] != 0) 
					break;	// row with space or already marked
			//
			if (i == map_w){		// mark full row
				for(i = 0; i < map_w; i++){
					mfx[i][j] = 1;
				}
			}
		}
	}
	
	/**
	 * Deleting area marked in mfx and move top rows down
	 */
	public void deleteArea(){
		int i, j, t;
		int cells = 0;
		if (cleanerTime < cleanerTimeMax)	// just not a time for cleaning
			return;
		
		cleanerTime = 0;			
		for(i = 0; i < map_w; i++){	// for each column
			for(j = 0; j < map_h; j++){	// for each row	
				if (mfx[i][j] == 0) continue;	// if area not marked - skip
				mfx[i][j] -= mfx_step;			// decrease mfx
				//Gdx.app.log("cc", ": " + mfx[i][j]);
				if (mfx[i][j] <= 0){			// just time to delete
					map[i][j] = 0;
					mfx[i][j] = 0;
					cells++;
					
					for(t = j; t < map_h; t++){
						mfx[i][t] = 0;
						if (t < map_h - 1){
							map[i][t] = map[i][t+1];	// upper neighbour
							mfx[i][t] = mfx[i][t+1]; 
						} else {
							map[i][t] = 0;				// top - use space
						}
						if (j > 0)
							j--;
					}
					
				}
			}
		}
		incScore(cells);
	}
	
	/**
	 * Check if figure can move somewhere or not.
	 * @return true, if all board blocked. It means that no turns available
	 */
	public boolean checkBlocked(){
		int i, j;
		for(i = 0; i < figure.bounds; i++){
			for(j = 0; j < figure.bounds; j++){
				if(figure.map[i][j] == 0) continue;
				if (figure.x + i >= map_w || figure.y + j >= map_h + 1)
					continue;
				if (figure.x + figure.origin_x < 0 || figure.y + figure.origin_x < 0)
					continue;
				if (map[figure.x + i][figure.y + j] != 0){
					//Gdx.app.log("Lock", "locked!");
					return true;
				}
			}
		}
		return false;
	}
		
	/**
	 * Move figure to offset
	 * @param dx Offset in X
	 * @param dy Offset in Y
	 * @return True, if figure moved, otherwise false
	 */
	public boolean moveFigure(int dx, int dy){
		if(!active) return false;
		int x = figure.x + dx;		// new container coods 
		int y = figure.y + dy;
		
		// if it out of walls in bottom and left
		// -1 in y sets for case 'landing to ground', that 'll checked below
		if (x + figure.origin_x < 0 || y + figure.origin_y < -1)
			return false;
		
		// if it breaks top and right walls
		if (x + figure.origin_x + figure.width > map_w
				|| y + figure.origin_y + figure.height > map_h)
			return false;
				
		int i, j;
		
		boolean contacts = false;
		boolean landing = false;
		
		// check area around figurre for contacts 
		for(i = 0; i < figure.bounds; i++){
			for(j = 0; j < figure.bounds; j++){
				if (figure.map[i][j] == 0) continue;	// figure space
				if (y + figure.origin_y >= 0){
					if (map[x + i][y + j] != 0){
						contacts = true;		// contacts with another figure
						break;
					}
				} else {
					landing = true; break;					
				}
			}
			if (landing)
				break;
		}
		//Gdx.app.log("contacts", (contacts) ? "true" : "false");
		if (contacts && dy < 0){	// contacts while move down = land
			landing = true;
		} else if (!contacts && y + figure.origin_y < 0){	
			// no contacts till bottom border == land to ground
			landing = true;
			//figure.y = y + figure.origin_y;
		} 
		if (contacts && !landing)
			return false;
		
		// landing
		if (landing){
			// loop through figure map
			int mx, my;
			for(i = 0; i < figure.bounds; i++)
				for(j = 0; j < figure.bounds; j++){					
					if (figure.map[i][j] == 0) continue;
					// we need to calculate figure position in 
					// board system coordinates
					mx = figure.x + i;					
					my = figure.y + j;
					// so, if figure has blank tiles in left and bottom borders - strip them all
					if (mx >= 0){
						if (my >= 0){
							map[mx][my] = figure.getColorAt(i, j);;
						}
					}
				}
			getNextFigure(); 
			return false;
		}
		
		// so, all area around is clean for movement and not landed - perform move!
		figure.x += dx;
		figure.y += dy;
		return true;
	}
	
	/**
	 * Rotate figure with specified direction to next position from {@link #figure}.rotation_map
	 * @param clockwide Dorection of rotation
	 * @return true if successfull rotated, otherwise false
	 */
	public boolean rotateFigure(boolean clockwise){
		if (!active) return false;
		
		if (rotateFigureEx(clockwise))
			return true;
		else {
			// rotate figure when it near left border
			if (figure.x + figure.origin_x <= 0)
				if (moveFigure(1, 0)){
					if (rotateFigure(clockwise))
						return true;
					else {
						moveFigure(-1, 0);
						return false;
					}
				}
			// rotate figure when it near right border
			if (figure.x + figure.bounds >= map_w){
				if (moveFigure(-1, 0)){
					if (rotateFigure(clockwise))
						return true;
					else {
						moveFigure(1, 0);
						return false;
					}
				}
			}
			/*
			// rotate figure when it locked
			boolean i = false, j;
			do {
				j = false;
				do {
					if (moveFigure((i) ? ((j) ? 1 : -1) : 0, (i) ? 0 : ((j) ? 1 : -1)))
						if (rotateFigureEx(clockwise))
							return true;
						else
							moveFigure((i) ? ((!j) ? 1 : -1) : 0, (i) ? 0 : ((!j) ? 1 : -1));
				} while(j = !j);
			} while (i = !i);
			*/
							
		}
		return false;
	}
	
	private boolean rotateFigureEx(boolean clockwise){
		if (!active) return false;
		
		// first, get new figure map
		figure.rotate(clockwise);
		
		// second, check board bounds
		if ((figure.x + figure.width + figure.origin_x > map_w) 
				|| (figure.y + figure.height + figure.origin_y > map_h) 
				|| figure.x + figure.origin_x < 0 
				|| figure.y + figure.origin_y < 0){
			// rotate back and exit
			figure.rotate(!clockwise); return false;
		}
		
		// third, check intersections with exists figures
		figure.visible = false;
		int i, j;
		for(i = 0; i < figure.bounds; i++){
			for(j = 0; j < figure.bounds; j++){
				if (figure.map[i][j] == 0) continue;		// free area
				if (map[figure.x + i][figure.y + j] != 0){	// place already occupied
					// rotate back and exit
					figure.rotate(!clockwise); return false;
				}
			}
		}
		figure.visible = true;
		// after all steps return positive result
		return true;
	}
	
	/**
	 * Clears map and mfx
	 */
	public void clear(){
		int i, j;
		for(i = 0; i < map_w; i++){
			for(j = 0; j < map_h; j++){
				map[i][j] = 0;
				mfx[i][j] = 0;
			}
		}
	}
	
	public void incScore(int count){
		if (count < 3) return;
		float k = (float)count / (float)this.map_w;
		score += MathUtils.ceil((float) (k * map_w * 1.45f));
		Gdx.input.vibrate(25 + (int) (k * 25));
	}
	
	
}
