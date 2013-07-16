package com.flaxtreme.tetroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

/**
 * Three-in-a-row version
 * @author Dmitry
 * @version 0.2
 */
public class BoardLines extends BoardClassic {
	
	public BoardLines() {
		super();
		
		Figure.maxFigure = 7;
		Figure.multiColor = true;
		//figure.reborn(map_w / 2, 0, this.colors.length);
	}
	
	/**
	 * Check is there place for new figure. If no - try move to another place. If still no - switch to GO state
	 */
	@Override
	public boolean checkGameOver() {
		if (!checkBlocked())
			return false;
		figure.visible = false;
		// move figure across board width and check
		int x = -figure.origin_x;
		int x_end	= map_w - (figure.origin_x + figure.width);
		do {
			figure.x = x++;
			if (!checkBlocked()){	// figure placed
				figure.visible = true;
				return false;
			}
		} while (x <= x_end);
		
		// still nothing - switch to GO state
		Gdx.app.log("Over", "Locked!");
		finished = true;
		setActive(false);	
		figure.visible = true;
		return true;
	}
	
	@Override
	public void findArea() {
		areaGravity();
		// удаление при наличии трех фигур одного цвета 
		// по вертикали или горизонтали
		int i, j, k;
		final int K = 3;
		// поиск вертикальных рядов
		for(i = 0; i < map_w; i++){
			for(j = 0; j < map_h - K; j++){
				if (map[i][j] == 0) continue;
				if (mfx[i][j] != 0) continue;
				for(k = 0; k < K; k++)
					if (map[i][j] != map[i][j + k])
						break;
				if (k == K){
					areaDeepWay(i, j);
					break;
				}
			}
		}
		// поиск горизонтальных рядов
		for(j = 0; j < map_h; j++){
			for(i = 0; i < map_w - K +1; i++){
				if (map[i][j] == 0) continue;
				if (mfx[i][j] != 0) continue;
				for(k = 0; k < K; k++)
					if (map[i][j] != map[i + k][j])
						break;
				if (k == K){
					areaDeepWay(i, j);
					break;					
				}
			}
		}
		deleteArea();
	}	
	

	@Override
	public void deleteArea() {
		int cells = 0;
		int i, j;
		for(i = 0; i < map_w; i++){
			for(j = 0; j < map_h; j++){
				if (mfx[i][j] == 0) continue;
				mfx[i][j] -= mfx_step;
				//Gdx.app.log("mfx", "x: " + x + " y: " + y + " v:" + mfx[x][y]);
				if (mfx[i][j] <= 0){
					map[i][j] = 0;
					mfx[i][j] = 0;
					cells++;
				}
			}
		}
		incScore(cells);
	}
	
	/**
	 * Recursive marks neighbor blocks with same color to delete
	 * @param x
	 * @param y
	 */
	public void areaDeepWay(int x, int y){
		byte c = map[x][y];
		mfx[x][y] = 1;		
		if (y + 1 < map_h	&& map[x][y+1] == c && mfx[x][y+1] == 0)	areaDeepWay(x, y+1);
		if (y - 1 >= 0 		&& map[x][y-1] == c && mfx[x][y-1] == 0)	areaDeepWay(x, y-1);
		if (x + 1 < map_w 	&& map[x+1][y] == c && mfx[x+1][y] == 0)	areaDeepWay(x+1, y);
		if (x - 1 >= 0 		&& map[x-1][y] == c && mfx[x-1][y] == 0)	areaDeepWay(x-1, y);
	}
	
	public void areaGravity(){
		// сбрасывание областей, расположенных выше удаленной области
		int i, j, k;
		for(i = 0; i < map_w; i++){
			for(j = 0; j < map_h-1; j++){
				if (map[i][j] == 0){
					for(k = j; k < map_h; k++){
						if (map[i][k] != 0){
							map[i][j++] = map[i][k];
							map[i][k] = 0;
						}
					}
					break;
				}
			}
		}
	}
	
	@Override
	public void incScore(int count) {
		if (count < 3) return;
		float ds = 3;
		ds += (2 * 1 + count - 1) / 2f;
		score += MathUtils.ceil(ds);
		Gdx.input.vibrate(25 + (count / 50) * 25);
	}
	
	@Override
	public void speedUp() {
		super.speedUp();
		
		if (score > 100 && Figure.maxFigure < 8){
			Figure.maxFigure = 8;
		} else if (score > 200 && Figure.maxFigure < 9){
			Figure.maxFigure = 9;
		}
	}

}
