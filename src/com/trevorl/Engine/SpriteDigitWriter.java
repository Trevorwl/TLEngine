package com.trevorl.Engine;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * A class which allows a swing component
 * to draw numbers that change using custom sprites.
 * 
 * Author: Trevor Lash
 * Modified: 1/1/2022
 */
public class SpriteDigitWriter {
	private ImageIcon[] sprites;
	private BufferedImage buffer;
	
	public int x;
	public int y;
	
	private int alignX;
	private boolean centerAlignmentSet;

	private int numberWidth;
	private int numberHeight;
	
	/**
	 * Gap between digits
	 */
	private int gap;

	public SpriteDigitWriter(String spriteSheetURL,
			int x, int y, 
			int r, int c, 
			int w, int h, 
			int gap) throws IOException{
		
		this.x = x;
		this.y = y;
		
		numberWidth = w;
		numberHeight = h;
		this.gap = gap;
		
		SpriteLoader loader = new SpriteLoader(spriteSheetURL,
				10, r, c, w, h);
		
		BufferedImage[] frames = loader.frames();
		
		sprites = new ImageIcon[loader.cells()];
		
		for (int i = 0; i < loader.cells() ; i++) {
			sprites[i] = new ImageIcon(frames[i]);
		}
	}
	
	public void centerAlign() {
		centerAlignmentSet = true;
		alignX = x;
	}
	
	public void setNumber(int number) {
		String[]digits = Integer.toString(number).split("");
		int places = digits.length;
		
		buffer = new BufferedImage(
				(places * numberWidth) + (gap * (places - 1)),
				numberHeight,
				BufferedImage.TYPE_INT_ARGB);
		
		Graphics bufferG = buffer.getGraphics();
		
		int ix = 0;
		
		for(int i = 0; i < places; i++) {
			ImageIcon numberImage = 
					sprites[Integer.parseInt(digits[i])];
			
			bufferG.drawImage(
					numberImage.getImage(), 
					ix, 0, 
					numberImage.getImageObserver());
			
			ix += (numberWidth + gap);
		}
		
		if(centerAlignmentSet) {
			x = alignX - buffer.getWidth() / 2;
		}
	}
	
	public void paint(Graphics g, JComponent component) {
		g.drawImage(buffer, x, y, component);
	}
	
}