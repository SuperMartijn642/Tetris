package SuperMartijn642.Tetris;

import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JFrame;

import SuperMartijn642.Tetris.Game.Game;
import SuperMartijn642.Tetris.Game.KeyHandler;

public class Main {
	
	public static JFrame frame;
	public static Canvas canvas;
	
	public static Game game;
	
	public static void main(String[] args) {
		frame = new JFrame("Tetris");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 1000);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.addKeyListener(new KeyHandler());
		canvas = new Canvas();
		canvas.setBackground(Color.BLACK);
		canvas.setSize(frame.getSize());
		canvas.addKeyListener(new KeyHandler());
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
		game = new Game();
		game.startGame();
	}
	
	public static int getHeight(){
		return canvas.getHeight();
	}
	
	public static int getWidth(){
		return canvas.getWidth();
	}
	
}
