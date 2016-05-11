package com.shivam.data;

/**
 * This class hold properties of Image linked to the item
 * */
public class Thumbnail {
	private final String source;	//Image Source Link
	private final int width;	//Image width
	private final int height;	//Image height
	
	
	public Thumbnail(String source, int width, int height) {
		this.source = source;
		this.width = width;
		this.height = height;
	}
	
	public String getSource() {
		return source;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}


}
