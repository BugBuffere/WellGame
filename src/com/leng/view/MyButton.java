package com.leng.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.leng.wellgame.Config;
import com.leng.wellgame.WellGame;
import com.leng.wellgame.WellGame.Spot;

public class MyButton extends JButton implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	
	private WellGame game;

	public MyButton(int id,WellGame game) {
		super();
		this.id = id;
		this.game = game;
		setBackground(Color.GRAY);
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (game.getGameState() != Config.NONE) {
			return;
		}
		Spot spot = SpotUtils.getSpot(id);
		game.user(spot.getX(), spot.getY());
	}

	@Override
	public String toString() {
		return "MyButton [id=" + id + ", game=" + game + "]";
	}
	
}
