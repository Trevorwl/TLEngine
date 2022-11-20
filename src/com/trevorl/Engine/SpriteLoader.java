package com.trevorl.Engine;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Loads sprites sheets at the size requested by the user.
 * Allows the user to remove sprite-loading logic from their code.
 * 
 * Author: Trevor Lash
 * Modified: 1/1/2022
 */
public class SpriteLoader {
	private BufferedImage[] frames;
	
	/**
	 * Loads a single sprite at the original resolution
	 */
	public SpriteLoader(String url) throws IOException {
		this(url, 1, 1, 1);
	}

	/**
	 * Loads multiple sprites at the original resolution
	 */
	public SpriteLoader(String url, int n, int r, 
			int c) throws IOException  {
		BufferedImage sprites = ImageIO.read(getClass().getResource(url));
		frames = new BufferedImage[n];
		
		int cell = 0;
		int x = 0;
		int y = 0;
		int dx = sprites.getWidth() / c;
		int dy = sprites.getHeight() / r;
		
		for (int row = 0; row < r; row++) {
			for (int col = 0; col < c; col++) {
				
				if (cell == n) {
					break;
				}
				
				frames[cell++] = sprites.getSubimage(x, y, dx, dy);

				x += dx;
			}
			
			if (cell == n) {
				break;
			}
			
			x = 0;
			y += dy;
		}
	}
	
	/**
	 * Loads a single sprite at the resolution
	 * specified by w and h
	 */
	public SpriteLoader(String url, int w, int h) throws IOException {
		this(url, 1, 1, 1, w, h);
	}

	/**
	 * Loads multiple sprites at the
	 * resolution specified by w and h
	 * 
	 * @param url The image file
	 * @param n Number of cells
	 * @param r Rows of cells
	 * @param c Cols of cells
	 * @param w desired width
	 * @param h desired height
	 * 
	 * @throws IOException 
	 */
	public SpriteLoader(String url, int n, int r, 
			int c, int w, int h) throws IOException  {
		BufferedImage sprites = ImageIO.read(getClass().getResource(url));
		frames = new BufferedImage[n];
		
		int cell = 0;
		int x = 0;
		int y = 0;
		int dx = sprites.getWidth() / c;
		int dy = sprites.getHeight() / r;
		
		for (int row = 0; row < r; row++) {
			for (int col = 0; col < c; col++) {
				
				if (cell == n) {
					break;
				}
				
				Image subImage = sprites.getSubimage(x, y, dx, dy);
				
				subImage = subImage.getScaledInstance(w, h, Image.SCALE_SMOOTH);
				
				ImageIcon ii = new ImageIcon(subImage);
				
				BufferedImage scaled = new BufferedImage(w, h, 
						BufferedImage.TYPE_INT_ARGB);
				
				Graphics g = scaled.getGraphics();
				
				g.drawImage(ii.getImage(), 0, 0, w, h, ii.getImageObserver());
				
				frames[cell++] = scaled;

				x += dx;
			}
			
			if (cell == n) {
				break;
			}
			
			x = 0;
			y += dy;
		}
	}
	
	/**
	 * Used when the loader contains only one sprite.
	 */
	public BufferedImage sprite() {
		return frames[0];
	}
	
	public BufferedImage frame(int index) {
		return frames[index];
	}

	public BufferedImage[] frames() {
		return frames;
	}
	
	public int cells() {
		return frames.length;
	}
	
}
