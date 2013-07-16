package com.flaxtreme.tetroid;

import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;

/**
 * Static class of figure definition handler
 * 
 * @author DimS
 * @version 0.1
 */
public class FigureDef {
	public static FigureDef[] figures;
	public static int figuresCount = 0;
	public int bound;
	public int rotations;
	public byte[][][] map;
	public int[] width;
	public int[] height;

	/**
	 * Creates array of figures
	 * @param size Total count of figures
	 */
	public static void initArray(int size) {
		figures = new FigureDef[size];
	}

	/**
	 * Add figure to figures array
	 * @param bound
	 * @param rotations
	 * @return
	 */
	public static int addFigure(int bound, int rotations) {
		figures[figuresCount] = new FigureDef(bound, rotations);
		return figuresCount++;
	}

	/**
	 * Add rotation to specified figure
	 * @param fid figure id
	 * @param map rotation map
	 */
	public static void addRotation(int fid, byte[][] map) {
		int r = figures[fid].rotations;
		int cols = 0;
		int rows = 0;
		int i, j, b = figures[fid].bound;
		// set map
		for (i = 0; i < b; i++) {
			for (j = 0; j < b; j++) {
				figures[fid].map[r][i][j] = map[i][j];
			}
		}
		// find free rows
		for(i = 0; i < b; i++){
			for(j = 0; j < b; j++)
				if (map[i][j] != 0)
					break;
			if (j != b)
				rows++;
		}
		// find free cols
		for(i = 0; i < b; i++){
			for(j = 0; j < b; j++)
				if (map[j][i] != 0)
					break;
			if (j != b)
				cols++;			
		}
		
		// save
		figures[fid].width[r] = rows;
		figures[fid].height[r] = cols;
		figures[fid].rotations += 1;
	}

	/**
	 * Reads figure definitions from file and batch add them to {@value #figures} array
	 * @param file External or internal file handle
	 */
	public static void readFigures(FileHandle file) {
		Scanner s = new Scanner(file.reader());

		int total = s.nextInt();
		initArray(total);
		
		for (; total > 0; total--) {
			int variants = s.nextInt();
			int bound = s.nextInt();
			int fid = addFigure(bound, variants);
			
			for (int vi = 0; vi < variants; vi++) {
				byte[][] map = new byte[bound][bound];
				for (int i = 0; i < bound; i++) {
					String ts = s.next();
					for (int j = 0; j < bound; j++) {
						map[i][j] = ((byte) (ts.codePointAt(j) - 48));
					}
				}
				addRotation(fid, map);
			}
		}
	}

	/**
	 * Constructor of new dynamic instance of figure def
	 * @param bound	figure bounds in TU
	 * @param rotations	maximum count of figure rotations 
	 */
	public FigureDef(int bound, int rotations) {
		this.bound = bound;
		this.rotations = 0;
		this.map = new byte[rotations][bound][bound];
		this.width = new int[rotations];
		this.height = new int[rotations];
	}
}
