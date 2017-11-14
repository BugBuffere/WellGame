package com.leng.wellgame;

public class StrategyDetails {
	
	private int userWin = 0;
	
	private int computerWin = 0;
	
	private int draw = 0;

	public StrategyDetails(int userWin, int computerWin, int draw) {
		super();
		this.userWin = userWin;
		this.computerWin = computerWin;
		this.draw = draw;
	}

	public int getUserWin() {
		return userWin;
	}

	public int getComputerWin() {
		return computerWin;
	}

	public int getDraw() {
		return draw;
	}

}
