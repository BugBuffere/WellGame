package com.leng.wellgame;

import java.util.ArrayList;
import java.util.List;

import com.leng.wellgame.WellGame.Spot;

public class GameRule {
	
	public GameRule(int[][] board){
		this.board = board;
	}
	
	public GameRule(){}
	
	private int[][] board;
	
	private final static int ROW = 0;
	
	private final static int COL = 1;
	
	private final static int OBLIQUE = 2;
	
	public void setBoard(int[][] board){
		if (board == null) {
			return;
		}
		if (board.length != board[0].length) {
			return;
		}
		this.board = board;
	}
	
	/**
	 * 检测游戏是否结束
	 * @return
	 */
	public int checkWin(){
		if (board == null) {
			return 0;
		}
		userWin.clear();
		computerWin.clear();
		boolean isWin = false;
		for (int i = 0; i < board.length; i++) {
			if (board[i][0] != 0) {
				continuityCount = 0;
				isWin = oblique(i, 0, board[i][0], ROW, false);
				if (isWin) {
					return board[i][0];
				}
			}
			if (i == 0) {
				for (int j = 0; j < board.length; j++) {
					if (board[i][j] != 0) {
						continuityCount = 0;
						isWin = oblique(i, j, board[i][j], COL, false);
						if (isWin) {
							return board[i][j];
						}
						if (j == 0) {
							continuityCount = 0;
							isWin = oblique(i, j, board[i][j], OBLIQUE, true);
						}else if (j == board.length -1) {
							continuityCount = 0;
							isWin = oblique(i, j, board[i][j], OBLIQUE, false);
						}
						if (isWin) {
							return board[i][j];
						}
					}
				}
			}
		}
		return Config.NONE;
	}

	private int continuityCount;
	
	//用户的胜点
	private List<Spot> userWin = new ArrayList<WellGame.Spot>();
	//电脑的胜点
	private List<Spot> computerWin = new ArrayList<WellGame.Spot>();
	
	/**
	 * 是否存在胜点
	 * @return
	 */
	public boolean isHashWin(){
		return !userWin.isEmpty() || !computerWin.isEmpty();
	}
	
	public List<Spot> getUsetWin(){
		return userWin;
	}
	
	public List<Spot> getComputerWin(){
		return computerWin;
	}
	
	/**
	 * 检测是否满足胜利条件
	 * @param x
	 * @param y
	 * @param value
	 * @param model
	 * @param isSine
	 * @return
	 */
	private boolean oblique(int x,int y,int value,int model,boolean isSine){
		boolean isWin  = board[x][y] == value;
		if (isWin) {
			continuityCount++;
			switch (model) {
			case ROW:
				y += 1;
				if (y >= board.length) {
					return isWin;
				}
				return oblique(x, y, value, model, isSine);
			case COL:
				x += 1;
				if (x >= board.length) {
					return isWin;
				}
				return oblique(x, y, value, model, isSine);
			case OBLIQUE:
				x += 1;
				if (isSine) {
					y += 1;
				}else{
					y -= 1;
				}
				if (x >= board.length || y < 0 || y >= board.length) {
					return isWin;
				}
				return oblique(x, y, value,model, isSine);
			default:
				break;
			}
		}else if (board[x][y] == 0) {
			if (continuityCount == board.length - 1) {
				if (value == Config.USER_VALUE) {
					userWin.add(new Spot(x, y));
				}else if (value == Config.COMPUTER_VALUE) {
					computerWin.add(new Spot(x, y));
				}
			}else if(continuityCount < board.length - 1){
				int nextX = 0;
				int nextY = 0;
				switch (model) {
				case ROW:
					nextX = x;
					nextY = y + 1;
					if (nextY >= board.length) {
						return isWin;
					}
					if (board[nextX][nextY] == value) {
						isWin = oblique(nextX, nextY, value, model, isSine);
					}
					break;
				case COL:
					nextX = x + 1;
					nextY = y;
					if (nextX >= board.length) {
						return isWin;
					}
					if (board[nextX][nextY] == value) {
						isWin = oblique(nextX, nextY, value, model, isSine);
					}
					break;
				case OBLIQUE:
					nextX = x + 1;
					if (isSine) {
						nextY = y +1;
					}else{
						nextY = y - 1;
					}
					if (nextX >= board.length || nextY < 0 || nextY >= board.length) {
						return isWin;
					}
					if (board[nextX][nextY] == value) {
						isWin = oblique(nextX, nextY, value, model, isSine);
					}
					break;
				}
				if (isWin && continuityCount == board.length -1) {

					if (value == Config.USER_VALUE) {
						userWin.add(new Spot(x, y));
					}else if (value == Config.COMPUTER_VALUE) {
						computerWin.add(new Spot(x, y));
					}
				}
				isWin = false;
				return isWin;
			}
		}
		return isWin;
	}
	/**
	 * 检测是否是胜点点
	 * @param board
	 * @param x
	 * @param y
	 * @return
	 */
	public int isWinSpot(int x,int y){
		if (board == null) {
			return Config.NONE;
		}
		if (x < 0 || x >= board.length || y < 0 || y >= board.length) {
			return Config.NONE;
		}
		boolean userSpot = isWinSpot(x, y, userWin);
		boolean computerSpot = isWinSpot(x, y, computerWin);
		if (userSpot && !computerSpot) {
			return Config.USER_VALUE;
		}else if (computerSpot && !userSpot) {
			return Config.COMPUTER_VALUE;
		}else if (userSpot && computerSpot) {
			return Config.ALL_VALUE;
		}
		return Config.NONE;
	}
	
	private boolean isWinSpot(int x,int y,List<Spot> spotList){
		if (spotList.isEmpty()) {
			return false;
		}
		for (int i = 0; i < spotList.size(); i++) {
			Spot spot = spotList.get(i);
			if (spot.getX() == x && spot.getY() == y) {
				spotList.remove(i);
				return true;
			}
		}
		return false;
	}

}
