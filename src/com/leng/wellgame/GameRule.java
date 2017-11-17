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
	
	private boolean debug = false;
	
	public void setBoard(int[][] board,boolean isDebug){
		if (board == null) {
			return;
		}
		if (board.length != board[0].length) {
			return;
		}
		debug = isDebug;
		this.board = board;
	}
	
	/**
	 * 检测游戏是否结束
	 * @return
	 */
	public int checkWin(){
		if (board == null) {
			return Config.NONE;
		}
		userWin.clear();
		computerWin.clear();
		boolean isWin = false;
		for (int i = 0; i < board.length; i++) {
			continuityCount = 0;
			isWin = oblique(i, 0, board[i][0], ROW, false);
			if (isWin) {
				if (!debug) {
					System.out.println("胜利row:: x=" + i + " y=" + 0);
					show();
				}
				return board[i][0];
			}
			if (i == 0) {
				for (int j = 0; j < board.length; j++) {
					continuityCount = 0;
					isWin = oblique(i, j, board[i][j], COL, false);
					if (isWin) {
						if (!debug) {
							System.out.println("胜利col:: x=" + i + " y=" + j);
							show();
						}
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
						if (!debug) {
							System.out.println("胜利oblique:: x=" + i + " y=" + j);
							show();
						}
						return board[i][j];
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
		if (value == 0) {
			int[] next = nextXY(x, y, model, isSine);
			int nextX = next[0];
			int nextY = next[1];
			if (nextX < 0 || nextX >= board.length || nextY < 0 || nextY >= board.length) {
				return false;
			}
			if (board[nextX][nextY] != 0) {
				boolean isWin = oblique(nextX, nextY, board[nextX][nextY], model, isSine);
				if (isWin && continuityCount == board.length -1) {
					switch (board[nextX][nextY]) {
					case Config.USER_VALUE:
						userWin.add(new Spot(x, y, Config.USER_VALUE));
						break;
					case Config.COMPUTER_VALUE:
						computerWin.add(new Spot(x, y, Config.COMPUTER_VALUE));
						break;
					default:
						break;
					}
				}
			}
			return false;
		}
		boolean isWin  = board[x][y] == value;
		if (isWin) {
			continuityCount++;
			int[] next = nextXY(x, y, model, isSine);
			int nextX = next[0];
			int nextY = next[1];
			if (nextX < 0 || nextX >= board.length || nextY < 0 || nextY >= board.length) {
				return isWin;
			}
			return oblique(nextX, nextY, value, model, isSine);
		}else if (board[x][y] == 0) {
			if (continuityCount == board.length - 1) {
				if (value == Config.USER_VALUE) {
					userWin.add(new Spot(x, y));
				}else if (value == Config.COMPUTER_VALUE) {
					computerWin.add(new Spot(x, y));
				}
			}else if(continuityCount < board.length - 1){
				int[] next = nextXY(x, y, model, isSine);
				int nextX = next[0];
				int nextY = next[1];
				if (nextX < 0 || nextX >= board.length || nextY < 0 || nextY >= board.length) {
					return isWin;
				}
				if (board[nextX][nextY] == value) {
					isWin = oblique(nextX, nextY, value, model, isSine);
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
	
	private int[] nextXY(int x,int y,int model,boolean isSine){
		int[] next = new int[2];
		int nextX = 0;
		int nextY = 0;
		switch (model) {
		case ROW:
			nextX = x;
			nextY = y + 1;
			break;
		case COL:
			nextX = x + 1;
			nextY = y;
			break;
		case OBLIQUE:
			nextX = x + 1;
			if (isSine) {
				nextY = y +1;
			}else{
				nextY = y - 1;
			}
			break;
		default:
			break;
		}
		next[0] = nextX;
		next[1] = nextY;
		return next;
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
	
	/**
	 * 展示棋盘
	 */
	public void show(){
		System.out.println("GameRule show!!!");
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				//System.out.print("[" + i + "," + j + "=" + board[i][j] + "]\t");
				System.out.print(board[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println("分割线------------------------------------------");
	}
	

}
