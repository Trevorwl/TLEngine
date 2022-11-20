package com.trevorl.Engine;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.Consumer;

import javax.swing.JComponent;

/**
 * This class represents a customized button that
 * allows the program to be designed without using a JButton.
 * 
 * Author: Trevor Lash
 * Last updated: 1/1/2022
 */
public class GameButton<T> implements MouseListener{
	private int x;
	private int y;
	private int width;
	private int height;

	private BufferedImage sprite;
	
	private Consumer<T> callBack;
	
	/**
	 * True when this button can't access its callback function,
	 * false otherwise.
	 */
	private boolean deactivated;

	public GameButton(String url,
			int x, int y, 
			int w, int h, Consumer<T> callBack) throws IOException {
		
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;

		SpriteLoader loader = new SpriteLoader(url, w, h);

		sprite = loader.sprite();
		
		this.callBack = callBack;
	}

	/**
	 * x coordinate becomes the center coordinate
	 */
	public void centerAlign() {
		x -= width / 2;
	}
	
	public boolean isDeactivated() {
		return deactivated;
	}
	
	public void deactivate() {
		deactivated = true;
	}
	
	public void activate() {
		deactivated = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(deactivated) {
			return;
		}
	
		if (e.getX() <= x + width
				&& e.getX() >= x
				&& e.getY() >= y
				&& e.getY()<= y + height) {

			callBack.accept(null);
		}
	}
	
	public void paint(Graphics g, JComponent component) {
		g.drawImage(sprite,x,y,component);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
