package com.leng.view;

import java.util.ArrayList;

import com.leng.wellgame.WellGame.Spot;

public class SpotUtils {
	
	private static ArrayList<Spot> spotList = new ArrayList<Spot>();
	
	static{
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				spotList.add(new Spot(i, j));
			}
		}
	}
	
	public static Spot getSpot(int index){
		return spotList.get(index);
	}

}
