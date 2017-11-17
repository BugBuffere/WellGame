package com.leng.wellgame;

import java.util.Map;

/**
 * 井字游戏
 * @author carry
 *
 */
public class WellGame {
	
	private int[][] board;
	
	private int size;
	
	private int count = 0;
	
	private GameRule mRule;
	
	private GameChangeListener mChangeListener;
	
	public WellGame(int size){
		this.size = size;
		board = new int[size][size];
		mRule = new GameRule(board);
	}
	
	private Spot history;
	
	private int gameState = Config.NONE;
	
	/**
	 * 用户下棋
	 * @param x
	 * @param y
	 */
	public boolean user(int x,int y){
		return addData(x, y, Config.USER_VALUE);		
	}
	/**
	 * 机器下棋
	 * @param x
	 * @param y
	 */
	public boolean computer(int x,int y){
		return addData(x, y, Config.COMPUTER_VALUE);
	}
	
	public void computerAuto(){
		Spot spot = analyticFunction();
		if (spot != null) {
			computer(spot.getX(), spot.getY());
		}
	}
	
	/**
	 * 解析函数
	 * @return
	 */
	private Spot analyticFunction(){
		Spot analytic = null;
		System.out.println("用户的胜点::" + mRule.getUsetWin());
		System.out.println("电脑的胜点::" + mRule.getComputerWin());
		//如果用户已经拿到胜点直接抢占胜点
		if (!mRule.getComputerWin().isEmpty()) {
			return mRule.getComputerWin().get(0);
		}
		if (!mRule.getUsetWin().isEmpty()) {
			return mRule.getUsetWin().get(0);
		}
		//System.out.println(history);
		DecisionTree decision = new DecisionTree(size, history);
		Map<Spot, StrategyDetails> strategyDetails = decision.analysisTree(decision.deriveTree());
		StrategyDetails oldDetails = null;
		for (Spot spot : strategyDetails.keySet()) {
			StrategyDetails details = strategyDetails.get(spot);
			if (oldDetails == null) {
				oldDetails = details;
				analytic = spot;
			}else{
				if (details.getComputerWin() > oldDetails.getComputerWin()) {
					oldDetails = details;
					analytic = spot;
				}
			}
			/*System.out.println("[" + spot.getX() + "," + spot.getY() + "]= {" 
			+ " userWin:" + details.getUserWin() + ", computerWin:" + details.getComputerWin() 
			+ ", draw:" + details.getDraw() + " }");*/
		}
		return analytic;
	}
	
	private boolean addData(int x,int y,int value){
		if (x < 0 || x >= board.length || y < 0 || y >= board.length) {
			return false;
		}
		if (board[x][y] != 0) {
			return false;
		}
		board[x][y] = value;
		count++;
		upGameChanged(x,y,value);
		addHistory(x, y, value);
		if (value == Config.USER_VALUE) {
			System.out.println("用户开始走……");
		}else if (value == Config.COMPUTER_VALUE) {
			System.out.println("电脑开始走……");
		}	
		show();
		System.out.println("记录栈::" + history);
		checkGame(value);
		return true;
	}
	
	private void upGameChanged(int x, int y, int value) {
		if (mChangeListener != null) {
			System.err.println("更新布局 x::" + x + " y::" + y + " vale::" + value);
			mChangeListener.update(x, y, value);
		}
	}
	/**
	 * 检测并输出游戏结果
	 */
	private void checkGame(int value){
		gameState = checkWin();
		System.out.println();
		if (mChangeListener != null) {
			switch (gameState) {
			case Config.NONE:
				if (value == Config.USER_VALUE) {
					computerAuto();
				}
				break;
			case Config.USER_VALUE:
				mChangeListener.gameOver("你胜利了!!!");
				break;
			case Config.COMPUTER_VALUE:
				mChangeListener.gameOver("电脑胜利!!");
				break;
			case Config.GAME_OVER:
				mChangeListener.gameOver("平局!");
				break;
			default:
				break;
			}
		}
	}
	
	private void addHistory(int x,int y,int value){
		if (history == null) {
			history = new Spot(x, y, value);
			return;
		}
		Spot task = history;
		Spot prev;
		for (;;) {
			prev = task;
			task = task.next();
			if (task == null) {
				break;
			}
		}
		prev.setNext(new Spot(x, y,value));
	}
	
	public int getGameState(){
		return gameState;
	}
	
	public int checkWin(){
		if (count < size) {
			return Config.NONE;
		}else if (count >= size * size) {
			return Config.GAME_OVER;
		}
		return mRule.checkWin();
	}
	
	/**
	 * 展示棋盘
	 */
	public void show(){
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				//System.out.print("[" + i + "," + j + "=" + board[i][j] + "]\t");
				System.out.print(board[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println("分割线------------------------------------------");
	}
	
	public int size(){
		return size;
	}
	
	public Spot history(){
		return history;
	}
	
	public void setGameChangeListener(GameChangeListener listener){
		mChangeListener = listener;
	}
	
	@SuppressWarnings("unused")
	private int coreXY(){
		return board.length % 2 == 0 ? 0 : board.length / 2 + 1;
	}
	
	public static class Spot {
		
		private int x;
		
		private int y;
		
		private int performer;
		
		private Spot next;
		
		public Spot(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		
		public Spot(int x,int y,int performer){
			this.x = x;
			this.y = y;
			this.performer = performer;
		}
			
		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
		
		public int performer(){
			return performer;
		}
		
		public void setNext(Spot spot){
			this.next = spot;
		}
		
		public Spot next(){
			return next;
		}

		@Override
		public String toString() {
			return "Spot [x=" + x + ", y=" + y + ", performer=" + performer
					+ ", next=" + next + "]";
		}
		
	}

	public void relase() {
		board = new int[size][size];
		mRule.setBoard(board,false);
		history = null;
		gameState = Config.NONE;
		count = 0;
	}
	
}
