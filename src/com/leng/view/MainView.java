package com.leng.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.leng.wellgame.Config;
import com.leng.wellgame.GameChangeListener;
import com.leng.wellgame.WellGame;

public class MainView {
	
	private static WellGame mWellGame;
	
	private static JFrame mJFrame;
	
	private static JTextField mJTable;
	
	private static ArrayList<MyButton> butList;
	
	private static JButton mJButton;

	private static JPanel contentJPanel;
	
	public static void crateWindow(){
		mJFrame = new JFrame("井字游戏");
		createWellGame();
		initView();
		mJFrame.setSize(600, 600);
		mJFrame.addWindowListener(myWindowListener);
		mJFrame.setVisible(true);
	}
	
	private static void createWellGame(){
		mWellGame = new WellGame(3);
		mWellGame.setGameChangeListener(new GameChangeListener() {
			
			@Override
			public void update(int x, int y, int value) {
				System.out.println("update x::" + x + " y::" + y + " value::" + value);
				MyButton but = null;
				switch (x) {
				case 0:
					but = butList.get(y);
					break;
				default:
					but = butList.get(3 * x + y);
					break;
				}
				System.out.println("but" + but);
				if (but != null) {
					if (value == Config.USER_VALUE) {
						but.setBackground(Color.BLUE);
					}else if (value == Config.COMPUTER_VALUE) {
						but.setBackground(Color.RED);
					}
				}
			}
			
			@Override
			public void gameOver(String msg) {
				System.out.println("输出游戏结果!!! " + msg);
				mJTable.setText(msg);
			}
		});
	}
	
	private static void initView(){
		/*Label label = new Label("This is my Window");
		mJFrame.add(label, BorderLayout.NORTH);*/
		JPanel topJPanel = new JPanel(new FlowLayout());
		topJPanel.setSize(600, 100);
		//topJPanel.add(createButton("重新开始", 180, 100));
		mJButton = new JButton("重新开始");
		mJButton.setSize(180, 100);
		mJButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (MyButton myButton : butList) {
					myButton.setBackground(Color.GRAY);
					mWellGame.relase();
				}
			}
		});
		Font f = new Font("重新开始", Font.BOLD, 20);
		mJButton.setFont(f);
		topJPanel.add(mJButton);
		mJTable = new JTextField();
		mJTable.setSize(300, 100);
		mJTable.setText("测试文本");
		topJPanel.add(mJTable);
		mJFrame.add(topJPanel,BorderLayout.NORTH);
		butList = new ArrayList<MyButton>();
		GridLayout gridLayout = new GridLayout(3, 3);
		contentJPanel = new JPanel(gridLayout);
		contentJPanel.setBackground(Color.BLACK);
		initContent(contentJPanel);
		mJFrame.add(contentJPanel);
	}
	
	private static void initContent(JPanel jPanel){
		for (int i = 0; i < 3 * 3; i++) {
			MyButton but = new MyButton(i, mWellGame);
			butList.add(but);
			jPanel.add(but);
		}
	}
	
	private static WindowListener myWindowListener = new WindowListener() {
		
		@Override
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("windowOpened");
		}
		
		@Override
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("windowIconified");
		}
		
		@Override
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("windowDeiconified");
		}
		
		@Override
		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("windowDeactivated");
		}
		
		@Override
		public void windowClosing(WindowEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("windowClosing");
			System.exit(0);
		}
		
		@Override
		public void windowClosed(WindowEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("windowClosed");
		}
		
		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("windowActivated");
		}
	};
	
	private static MouseListener myMouseListener = new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("mouseReleased");
		}
		
		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("mousePressed");
		}
		
		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("mouseExited");
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("mouseEntered");
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("mouseClicked");
		}
	};

}
