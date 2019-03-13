package SuperMartijn642.Tetris.Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import SuperMartijn642.Tetris.Main;
import SuperMartijn642.Tetris.Game.Block.Block;

public class Game {

	private static final int PREFERREDFPS = 100;
	private static final int PREFERREDTPS = 100;
	
	public static final int BLOCKMOVETIME = PREFERREDFPS / 10;
	
	public boolean stopGame = false;
	public int currentFps;
	public int currentTps;
	public int renderScaleX;
	public int renderScaleY;
	
	public int xRows;
	public int yRows;
	public int blockMoveTime = 0;
	public Block activeBlock;
	public ArrayList<Block> blocks;
	
	public boolean dead = false;
	
	public void startGame(){
		this.init();
		double deltaFps = 0;
		double timePerFrame = 1000000000 / PREFERREDFPS;
		double timePerTick = 1000000000 / PREFERREDTPS;
		double deltaTps = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int frames = 0;
		int ticks = 0;
		while (!this.stopGame) {
			now = System.nanoTime();
			deltaFps += (now - lastTime) / timePerFrame;
			deltaTps += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;
			if(deltaFps >= 1){
				this.render();
				frames++;
				deltaFps--;
			}
			if(deltaTps >= 1){
				this.tick();
				ticks++;
				deltaTps--;
			}
			if(timer >= 1000000000){
				this.currentFps = frames;
				this.currentTps = ticks;
				frames = 0;
				ticks = 0;
				timer = 0;
			}
			if(deltaFps < 1 && deltaTps < 1){
				try {
					Thread.sleep(1);
				} catch (Exception e) {e.printStackTrace();}
			}
		}
		this.stop();
	}
	
	private void init(){
		Block.init();
		this.renderScaleX = Main.getWidth() / 25;
		this.renderScaleY = Main.getHeight() / 50;
		this.xRows = Main.getWidth() / this.renderScaleX;
		this.yRows = Main.getHeight() / this.renderScaleY;
		this.activeBlock = new Block();
		this.blocks = new ArrayList<>();
	}
	
	private void render(){
		if(Main.canvas.getBufferStrategy() == null)
			Main.canvas.createBufferStrategy(3);
		BufferStrategy bufferStrategy = Main.canvas.getBufferStrategy();
		Graphics graphics = bufferStrategy.getDrawGraphics();
		graphics.clearRect(0, 0, Main.getWidth(), Main.getHeight());
		
		this.activeBlock.render(graphics, this);
		for(Block block : this.blocks)
			block.render(graphics, this);
		if(this.dead){
			graphics.setColor(Color.WHITE);
			graphics.setFont(new Font("", Font.PLAIN, 30));
			graphics.drawString("You died!", Main.getWidth() / 2 - graphics.getFontMetrics().stringWidth("You died!") / 2, Main.getHeight() / 2);
		}
		
		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("", Font.PLAIN, 10));
		graphics.drawString("Fps: " + this.currentFps + "/" + PREFERREDFPS + " Tps: " + this.currentTps + "/" + PREFERREDTPS, 5, 10);
		
		bufferStrategy.show();
		graphics.dispose();
	}
	
	private void tick(){
		if(this.dead)
			return;
		this.blockMoveTime++;
		if(this.blockMoveTime >= BLOCKMOVETIME){
			for(Block block : this.blocks)
				block.tick(this);
			int xMove = 0;
			if((KeyHandler.hit_key_a || KeyHandler.key_a) && !(KeyHandler.hit_key_d || KeyHandler.key_d)){
				xMove = -1;
				for(Position2D part : this.activeBlock.parts)
					if(this.activeBlock.pos.x + part.x - 1 < 0){
						xMove = 0;
						break;
					}
			}else if(!(KeyHandler.hit_key_a || KeyHandler.key_a) && (KeyHandler.hit_key_d || KeyHandler.key_d)){
				xMove = 1;
				for(Position2D part : this.activeBlock.parts)
					if(this.activeBlock.pos.x + part.x + 1 == this.xRows){
						xMove = 0;
						break;
					}
			}
			if(KeyHandler.key_a || KeyHandler.key_d){
				boolean move = true;
				blockloop: for(Block block : this.blocks)
					for(Position2D pos : block.parts)
						for(Position2D part : this.activeBlock.parts)
							if(block.pos.x + pos.x == this.activeBlock.pos.x + part.x + xMove && block.pos.y + pos.y == this.activeBlock.pos.y + part.y){
								move = false;
								break blockloop;
							}
				if(move)
					this.activeBlock.pos.x += xMove;
			}
			if(!this.activeBlock.tick(this)){
				this.blocks.add(this.activeBlock);
				this.activeBlock = null;
				this.activeBlock = new Block();
				if(!this.activeBlock.doesFit())
					this.dead = true;
			}
			else if(KeyHandler.key_s && !this.activeBlock.tick(this)){
				this.blocks.add(this.activeBlock);
				this.activeBlock = null;
				this.activeBlock = new Block();
				if(!this.activeBlock.doesFit())
					this.dead = true;
			}
			this.blockMoveTime = 0;
			for(int y = 0; y < this.yRows; y++){
				boolean full = true;
				ArrayList<Block> blocksInRow = new ArrayList<>();
				for(int x = 0; x < this.xRows; x++){
					boolean hasX = false;
					blockloop: for(Block block : this.blocks){
						for(Position2D part : block.parts)
							if(block.pos.y + part.y == y && block.pos.x + part.x == x){
								hasX = true;
								if(!blocksInRow.contains(block))
									blocksInRow.add(block);
								break blockloop;
							}
					}
					if(!hasX){ full = false; break; }
				}
				
				if(full){
					for(int a = 0; a < blocksInRow.size(); a++)
						blocksInRow.get(a).removeParts(y);
				}
			}
			if(KeyHandler.hit_key_a)
				KeyHandler.hit_key_a = false;
			if(KeyHandler.hit_key_d)
				KeyHandler.hit_key_d = false;
		}
	}
	
	private void stop(){
		
	}
	
}
