package com.flaxtreme.tetroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
/**
 * Board figure class
 * 
 * Figures holds in area, determined with {@link #bounds}
 * Width and height is visible bounds of figure
 * Origin is shifting visible bounds relavitive to figure container
 * Location of left bottom corner sets with {@link #setPosition(int, int)}
 * Colors will be generated in range [1, max_color];
 * 
 * @author DimS
 * @version 0.6
 */
public class Figure {
	/**
	 * Location of left bottom corner in board coordinates
	 */
	public int x, y;
	public boolean visible = true;
	/**
	 * Bounds is figure square holder, width and height - size of visible rows and cols 
	 */
	public int width, height, bounds;
	/**
	 * Origin is free space between figure borders and first drawable elements
	 */
	public int origin_x, origin_y;
	public byte[][] map;
	public byte[] map_color;
	public int type;
	int rotations;
	int rotation;
	/**
	 * Unique identifier for new figure
	 */
	public static int uid_counter = 0;
	public int fid;
	/**
	 * Using this value we can manage count of figure types on board
	 */
	public static int maxFigure = 0;
	/**
	 * This field manages multicoloring of figure 
	 */
	public static boolean multiColor = true;
	/**
	 * Keeps last generated color in monocolor mode for better look'n'feel
	 */
	private static byte lastColor = 0;
	
	/**
	 * Figure constructor
	 * @param max_color Figure color will be generated in range [1, max_color]
	 */
	public Figure(int max_color) {
		if (FigureDef.figures == null){
			FigureDef.readFigures(Gdx.files.internal("game/figures.txt"));
		}
		
		reborn(4, 4, max_color);
	}

	/**
	 * Reinits figure to new position with new color
	 * @param nx map width
	 * @param ny map height
	 * @param max_color Color will be generated in range [1, max_color]
	 */
	public void reborn(int nx, int ny, int max_color) {
		fid = uid_counter++;
		if (maxFigure < 1 || maxFigure > FigureDef.figuresCount)
			maxFigure = FigureDef.figuresCount;
		
		this.type = MathUtils.random(maxFigure - 1);
		
		this.bounds = FigureDef.figures[this.type].bound;
		this.rotation = MathUtils.random(FigureDef.figures[this.type].rotations - 1) - 1;
		rotate(true);

		// inits color map
		// first, find color count
		int i, j;
		int max_color_id = 0;
		for(i = 0; i < this.bounds; i++){
			for(j = 0; j < this.bounds; j++){
				if (map[i][j] > max_color_id)
					max_color_id = map[i][j];
			}
		}
		// second, create & populate color map
		map_color = new byte[max_color_id+1];
		if (multiColor){
			for(i = 1; i <= max_color_id; i++){
				map_color[i] = (byte)MathUtils.random(1, max_color);
			}
		} else {
			byte bicolor;
			do {
				bicolor = (byte)MathUtils.random(1, max_color);
			} while (max_color > 1 && bicolor == lastColor);
			lastColor = bicolor;
			for(i = 1; i <= max_color_id; i++)
				map_color[i] = bicolor;
		}
		
		nx = MathUtils.ceil((nx - 1 - (this.width  + this.origin_x)) / 2f);		
		ny -= origin_y;
		setPosition(nx, ny);
		
		//Gdx.app.log("New figure", "uid: " + fid + " y: " + ny);
	}
	
	public byte getColorAt(int x, int y){
		return map_color[map[x][y]];
	}

	/**
	 * Rotate figure. Sets next rotation map as main and update width/height
	 * @param clockwise Direction of rotation
	 */
	public void rotate(boolean clockwise) {
		if (clockwise)
			this.rotation += 1;
		else
			this.rotation -= 1;
		if (this.rotation < 0)
			this.rotation = (FigureDef.figures[this.type].rotations - 1);
		if (this.rotation == FigureDef.figures[this.type].rotations)
			this.rotation = 0;
		
		this.width = FigureDef.figures[this.type].width[this.rotation];
		this.height = FigureDef.figures[this.type].height[this.rotation];
		if ((this.map == null) || (this.map.length != this.bounds)) {
			this.map = new byte[this.bounds][this.bounds];
		}
		int i, j;
		origin_x = origin_y = 0;
		for (i = 0; i < this.bounds; i++)
			for (j = 0; j < this.bounds; j++)
				this.map[i][j] = FigureDef.figures[this.type].map[this.rotation][i][j];
		
		// calc origin x
		for(i = 0; i < bounds; i++){
			for(j = 0; j < bounds; j++)
				if (map[i][j] != 0) break;
			if (j == bounds) origin_x++;
			else break;
		}
		// calc origin y
		for(i = 0; i < bounds; i++){
			for(j = 0; j < bounds; j++)
				if (map[j][i] != 0) break;
			if (j == bounds) origin_y++;
			else break;
		}		
	}

	/**
	 * Setter for position
	 * @param x
	 * @param y
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = (y - this.height);
	}
}
