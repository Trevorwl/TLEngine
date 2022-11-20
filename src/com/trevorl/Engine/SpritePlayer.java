package com.trevorl.Engine;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * This class allows the client to
 * iterate through sprite frames
 * without adding additional logic.
 * For thread safety, a 
 * timer is not provided.
 * Instead, a client calls
 * update() directly or
 * indirectly using its own timer.
 * 
 * Author: Trevor Lash
 * Modified: 1/1/2022
 */
public class SpritePlayer {
	private BufferedImage[] frames;
	private int frameIndex;
	
	public SpritePlayer(BufferedImage[] frames) {
		this.frames = frames;
		frameIndex = 0;
	}
	
	public void update() {
		frameIndex  = ++frameIndex % frames.length;
	}
	
	public void reset() {
		frameIndex = 0;
	}

	public Image currentFrame() {
		return frames[frameIndex];
	}

}
