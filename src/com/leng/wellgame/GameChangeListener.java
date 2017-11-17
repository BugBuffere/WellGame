package com.leng.wellgame;

public interface GameChangeListener {
	
	void update(int x,int y,int value);
	
	void gameOver(String msg);

}
