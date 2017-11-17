package com.leng.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import com.leng.utils.Resource;
import com.leng.wellgame.Config;
import com.leng.wellgame.DecisionTree;
import com.leng.wellgame.GameRule;
import com.leng.wellgame.StrategyDetails;
import com.leng.wellgame.WellGame;
import com.leng.wellgame.WellTree;
import com.leng.wellgame.WellGame.Spot;

public class Test {
	
	private static BufferedWriter bos;
	
	public static void main(String[] args) {
		testGameRule();
	}
	
	public static void startGame(){
		WellGame game = new WellGame(3);
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		int win = 0;
		while ((win = game.checkWin()) == 0) {
			System.out.print("请输入::");
			String line = sc.nextLine();
			if ("exit".equals(line)) {
				break;
			}
			String[] strs = line.split(",");
			int x = Integer.parseInt(strs[0]);
			int y = Integer.parseInt(strs[1]);
			game.user(x, y);
			win = game.checkWin();
			/*if (win == 0) {
				game.computerAuto();
			}else{
				break;
			}*/
		}
		switch (win) {
		case Config.USER_VALUE:
			System.out.println("玩家赢了!");
			break;
		case Config.COMPUTER_VALUE:
			System.out.println("电脑赢了！");
			break;
		case Config.GAME_OVER:
			System.out.println("平局！！");
			break;
		default:
			break;
		}
	}
	
	public static void testTree(){
		Spot hository = new Spot(0, 0,Config.USER_VALUE);
		/*Spot computer1 = new Spot(1, 1,Config.COMPUTER_VALUE);
		hository.setNext(computer1);
		Spot user1 = new Spot(0, 1,Config.USER_VALUE);
		computer1.setNext(user1);
		Spot computer2 = new Spot(0, 2, Config.COMPUTER_VALUE);
		user1.setNext(computer2);
		System.out.println(hository);*/
		DecisionTree decisionTree = new DecisionTree(3, hository);
		long start = System.currentTimeMillis();
		System.err.println("开始生成决策树……");
		WellTree tree = decisionTree.deriveTree();
		System.out.println("生成决策树完成");
		System.out.println("用时::" + (System.currentTimeMillis() -  start));
		/*init();
		showChild(tree);
		close();*/
		Map<Spot, StrategyDetails> strategyDetails = decisionTree.analysisTree(tree);
		for (Spot spot : strategyDetails.keySet()) {
			StrategyDetails details = strategyDetails.get(spot);
			System.out.println("[" + spot.getX() + "," + spot.getY() + "]= {" 
			+ " userWin:" + details.getUserWin() + ", computerWin:" + details.getComputerWin() 
			+ ", draw:" + details.getDraw() + " }");
		}
		/**/
	}
	
	public static void testGameRule(){
		int[][] board = new int[3][3];
		/**
		 * 	1	1	-1
		 * 	0	-1	0
		 *  1	0	0
		 */
		board[1][1] = 1;
		board[2][1] = 1;
		board[2][2] = -1;
		GameRule rule = new GameRule(board);
		rule.checkWin();
		System.out.println("userWin::" + rule.getUsetWin());
		System.out.println("computerWin::" + rule.getComputerWin());
	}
	
	public static void showChild(WellTree parent){
		if (parent == null) {
			file("-----------------------------------------");
			return;
		}
		if (parent.childLength() == 0) {
			file(parent.toString());
			return;
		}
		while (parent.hashNext()) {
			WellTree child = parent.next();
			showChild(child);
		}
	}
	
	public static void init(){
		try {
			File file = new File(Resource.asInstance().getFilePath("WellTree.txt"));
			bos = new BufferedWriter(new FileWriter(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void file(String msg){
		if (bos != null) {
			try {
				bos.write(msg);
				bos.newLine();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void close(){
		if (bos != null) {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bos = null;
		}
	}

}
