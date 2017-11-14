package com.leng.wellgame;

import java.util.HashMap;
import java.util.Map;

import com.leng.wellgame.WellGame.Spot;

/**
 * 决策树
 * @author carry
 *
 */
public class DecisionTree {
	
	private Spot stack;
	
	private int size;
	
	public DecisionTree(int size,Spot history){
		this.size = size;
		stack = history;
		mRule = new GameRule();
	}
	/**
	 * 用户胜
	 */
	private int userWin = 0;
	/**
	 * 电脑胜
	 */
	private int computerWin = 0;
	/**
	 * 和局
	 */
	private int draw = 0;
	
	private GameRule mRule;
	
	public Map<Spot, StrategyDetails> analysisTree(WellTree tree){
		if (tree == null) {
			return null;
		}
		WellTree child = null;
		while (tree.childLength() == 1) {
			tree.hashNext();
			child = tree.next();
			tree = child;
		}
		if (child == null) {
			child = tree;
		}
		Map<Spot, StrategyDetails> details = new HashMap<WellGame.Spot, StrategyDetails>();
		while (child.hashNext()) {
			userWin = 0;
			computerWin = 0;
			draw = 0;
			WellTree analysis = child.next();
			analysis(analysis);
			Spot spot = new Spot(analysis.getX(), analysis.getY());
			StrategyDetails strategyDetatils = new StrategyDetails(userWin, computerWin, draw);
			details.put(spot, strategyDetatils);
		}
		return details;
	}
	
	private void analysis(WellTree tree){
		while (tree.hashNext()) {
			WellTree childs = tree.next();
			/*//判断是否存在胜点
			if (mRule.isHashWin()) {
				int winSpot = mRule.isWinSpot(childs.getX(), childs.getY());
				switch (winSpot) {
				case Config.NONE:
					
					break;
				case Config.USER_VALUE:
					if (childs.performer() == winSpot) {
						userWin++;
					}
					return;
				case Config.COMPUTER_VALUE:
					if (childs.performer() == winSpot) {
						computerWin++;
					}
					return;
				case Config.ALL_VALUE:
					if (childs.performer() == Config.USER_VALUE) {
						userWin++;
					}else if (childs.performer() == Config.COMPUTER_VALUE) {
						computerWin++;
					}
					return;
				default:
					break;
				}
			}*/
			int[][] board = initBoard();
			board[childs.getX()][childs.getY()] = childs.performer();
			WellTree parent = null;
			WellTree deatilsTree = childs;
			while ((parent = deatilsTree.parent()) != null) {
				board[parent.getX()][parent.getY()] = parent.performer();
				deatilsTree = parent;
			}
			mRule.setBoard(board);
			int win = mRule.checkWin();
			switch (win) {
			case Config.NONE:
				if (childs.childLength() == 0) {
					draw++;
				}else{
					analysis(childs);
				}
				break;
			case Config.USER_VALUE:
				userWin++;
				break;
			case Config.COMPUTER_VALUE:
				computerWin++;
				break;
			}
		}
		return;
	}
	
	/**
	 * 生成决策树
	 * @return
	 */
	public WellTree deriveTree(){
		if (stack == null) {
			return null;
		}
		WellTree tree = null;
		WellTree parent = null;
		Spot prev = null;
		Spot next = stack;
		for (;;) {
			prev = next;
			next = next.next();
			if (tree == null) {
				tree = new WellTree(prev.getX(), prev.getY(), prev.performer());
			}else if (parent == null) {
				parent = new WellTree(prev.getX(),prev.getY(),prev.performer());
				tree.addChild(parent);
				parent.setParent(tree);
			}else{
				WellTree child = new WellTree(prev.getX(), prev.getY(), prev.performer());
				parent.addChild(child);
				child.setParent(parent);
				parent = child;
			}
			if (next == null) {
				break;
			}
		}
		if (parent == null) {
			parent = tree;
		}
		/*System.out.println("stack:: " + stack);
		System.out.println("parent:: " + parent);*/
		createTree(parent);
		return tree;
	}
	/**
	 * 生成决策树分支
	 * @param parent
	 */
	private void createTree(WellTree parent){
		if (parent == null) {
			return;
		}
		int[][] board = initBoard();
		board[parent.getX()][parent.getY()] = parent.performer();
		WellTree prev = null;
		WellTree next = parent;
		for (;;) {
			prev = next.parent();
			if (prev == null) {
				break;
			}
			board[prev.getX()][prev.getY()] = prev.performer();
			next = prev;
		}
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == 0) {
					WellTree tree = new WellTree(i, j, 
							parent.performer() == Config.USER_VALUE ? Config.COMPUTER_VALUE : Config.USER_VALUE);
					int layer = parent.layer() + 1;
					tree.setLayer(layer);
					parent.addChild(tree);
					tree.setParent(parent);					
					createTree(tree);
				}
			}
		}
	}
	
	private int[][] initBoard(){
		return new int[size][size];
	}

}
