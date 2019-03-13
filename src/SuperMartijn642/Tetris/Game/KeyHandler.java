package SuperMartijn642.Tetris.Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
	
	public static boolean key_a = false;
	public static boolean hit_key_a = false;
	public static boolean key_w = false;
	public static boolean key_d = false;
	public static boolean hit_key_d = false;
	public static boolean key_s = false;
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_A){
			key_a = true;
			hit_key_a = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_W)
			key_w = true;
		else if(e.getKeyCode() == KeyEvent.VK_D){
			key_d = true;
			hit_key_d = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_S)
			key_s = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_A)
			key_a = false;
		if(e.getKeyCode() == KeyEvent.VK_W)
			key_w = false;
		if(e.getKeyCode() == KeyEvent.VK_D)
			key_d = false;
		if(e.getKeyCode() == KeyEvent.VK_S)
			key_s = false;
	}
	
}
