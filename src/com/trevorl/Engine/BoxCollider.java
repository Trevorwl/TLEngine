package com.trevorl.Engine;

public class BoxCollider {
	int x;
	int y;
	int width;
	int length;
	
	public BoxCollider(int x, int y, int width, int length) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.length = length;
	}
	
	public boolean hasCollision(BoxCollider other) {
		return  x < other.x + other.width 
		        && x + width > other.x 
		        &&  y < other.y + other.length 
		        &&  length + y > other.y;
	}
	
}
