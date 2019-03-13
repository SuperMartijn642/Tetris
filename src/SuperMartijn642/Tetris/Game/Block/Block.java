package SuperMartijn642.Tetris.Game.Block;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import SuperMartijn642.Tetris.Main;
import SuperMartijn642.Tetris.Game.Game;
import SuperMartijn642.Tetris.Game.Position2D;

public class Block {
	
	public static ArrayList<ArrayList<Position2D>> blocks;
	
	public static void init(){
		ArrayList<Position2D[]> rawBlocks = new ArrayList<>();
		rawBlocks.add(new Position2D[] {new Position2D(0, 1), new Position2D(0, 0), new Position2D(0, -1)});
		rawBlocks.add(new Position2D[] {new Position2D(-1, 0), new Position2D(0, 0), new Position2D(1, 0)});
		rawBlocks.add(new Position2D[] {new Position2D(0, 0), new Position2D(1, 0), new Position2D(0, 1)});
		rawBlocks.add(new Position2D[] {new Position2D(0, -1), new Position2D(0, 0), new Position2D(1, 0)});
		rawBlocks.add(new Position2D[] {new Position2D(-1, 0), new Position2D(0, 0), new Position2D(0, 1)});
		rawBlocks.add(new Position2D[] {new Position2D(0, -1), new Position2D(0, 0), new Position2D(1, 0)});
		rawBlocks.add(new Position2D[] {new Position2D(0, 2), new Position2D(0, 1), new Position2D(0, 0), new Position2D(0, -1)});
		rawBlocks.add(new Position2D[] {new Position2D(-2, 0), new Position2D(-1, 0), new Position2D(0, 0), new Position2D(1, 0)});
		rawBlocks.add(new Position2D[] {new Position2D(1, -1), new Position2D(1, 0), new Position2D(0, 0), new Position2D(0, 1)});
		rawBlocks.add(new Position2D[] {new Position2D(1, 1), new Position2D(1, 0), new Position2D(0, 0), new Position2D(0, -1)});
		rawBlocks.add(new Position2D[] {new Position2D(-1, 1), new Position2D(0, 1), new Position2D(0, 0), new Position2D(1, 0)});
		rawBlocks.add(new Position2D[] {new Position2D(1, 1), new Position2D(0, 1), new Position2D(0, 0), new Position2D(-1, 0)});
		rawBlocks.add(new Position2D[] {new Position2D(1, 1), new Position2D(0, 1), new Position2D(0, 0), new Position2D(1, 0)});
		blocks = new ArrayList<>();
		for(Position2D[] pos : rawBlocks){
			ArrayList<Position2D> array = new ArrayList<>();
			for(Position2D pos2 : pos)
				array.add(pos2);
			blocks.add(array);
		}
	}
	
	public Position2D pos;
	public ArrayList<Position2D> parts;
	public Color color;
	public boolean canMoveDown = false;
	
	public Block(Position2D pos, ArrayList<Position2D> parts, Color color){
		this.pos = pos;
		this.parts = parts;
		this.color = color;
	}
	
	@SuppressWarnings("unchecked")
	public Block(Position2D pos){
		this.pos = pos;
		this.parts = (ArrayList<Position2D>)blocks.get(new Random().nextInt(blocks.size())).clone();
		this.color = new Color(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat());
	}
	
	public Block() {
		this(new Position2D((int)(Main.game.xRows / 2F), 0));
	}
	
	public void render(Graphics graphics, Game game){
		graphics.setColor(this.color);
		if(this.canMoveDown)
			for(Position2D pos : this.parts)
				graphics.fillRect((int)((this.pos.x + pos.x) * game.renderScaleX + 1), (int)((this.pos.y + pos.y + (float)game.blockMoveTime / Game.BLOCKMOVETIME) * game.renderScaleY + 1), game.renderScaleX - 2, game.renderScaleY - 2);
		else
			for(Position2D pos : this.parts)
				graphics.fillRect((int)((this.pos.x + pos.x) * game.renderScaleX + 1), (int)((this.pos.y + pos.y) * game.renderScaleY + 1), game.renderScaleX - 2, game.renderScaleY - 2);
	}
	
	public boolean tick(Game game){
		boolean moved = true;
		for(Position2D part : this.parts)
			if(this.pos.y + part.y == game.yRows - 1)
				moved = false;
		if(moved)
			blockloop: for(Block block : game.blocks){
				if(block != this)
					for(Position2D pos : block.parts){
						for(Position2D part : this.parts)
							if(block.pos.y + pos.y == this.pos.y + part.y + 1 && block.pos.x + pos.x == this.pos.x + part.x){
								moved = false;
								break blockloop;
							}
					}
			}
		if(moved)
			this.pos.y++;
		this.canMoveDown = true;
		for(Position2D part : this.parts)
			if(this.pos.y + part.y == game.yRows - 1)
				this.canMoveDown = false;
		if(this.canMoveDown)
			blockloop: for(Block block : game.blocks){
				if(block != this)
					for(Position2D pos : block.parts){
						for(Position2D part : this.parts)
							if(block.pos.y + pos.y == this.pos.y + part.y + 1 && block.pos.x + pos.x == this.pos.x + part.x){
								this.canMoveDown = false;
								break blockloop;
							}
					}
			}
		return moved;
	}
	
	public void removeParts(int height){
		ArrayList<Position2D> blocks1 = new ArrayList<>();
		ArrayList<Position2D> blocks2 = new ArrayList<>();
		for(Position2D part : this.parts)
			if(this.pos.y + part.y < height)
				blocks1.add(part);
			else if(this.pos.y + part.y > height)
				blocks2.add(part);
		if(blocks1.size() == 0 && blocks2.size() != 0)
			this.parts = blocks2;
		else if(blocks1.size() != 0 && blocks2.size() == 0)
			this.parts = blocks1;
		else if(blocks1.size() == 0 && blocks2.size() == 0)
			Main.game.blocks.remove(this);
		else{
			this.parts = blocks1;
			Main.game.blocks.add(new Block(this.pos.clone(), blocks2, this.color));
		}
	}
	
	public boolean doesFit(){
		boolean fits = true;
		blockloop: for(Block block : Main.game.blocks)
			for(Position2D pos : block.parts)
				for(Position2D part : this.parts)
					if(block.pos.x + pos.x == this.pos.x + part.x && block.pos.y + pos.y == this.pos.y + part.y){
						fits = false;
						break blockloop;
					}
		return fits;
	}
	
}
