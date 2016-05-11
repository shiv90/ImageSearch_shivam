package com.shivam.data;

/**
 * Bean class to store Searched JSON Data 
 * */
public class FeedItem {
	
	
	private final String page_id;	//Item Page Id
	private final String title;	//Item Title
	private final Thumbnail thumbnail;	//Item Thumbnail
	
	public FeedItem(String page_id, String title, Thumbnail thumbnail) {
		this.page_id = page_id;
		this.title = title;
		this.thumbnail = thumbnail;
	}

	public String getPage_id() {
		return page_id;
	}

	public String getTitle() {
		return title;
	}

	public Thumbnail getThumbnail() {
		return thumbnail;
	}
	
	/*
	 * Builder class to build immutable item object
	 * */
	public static class FeedItemBuilder{
		
		private final String page_id;
		private final String title;
		private Thumbnail thumbnail;
		
		public FeedItemBuilder(String page_id, String title) {
			super();
			this.page_id = page_id;
			this.title = title;
		}
		
		
		public FeedItemBuilder setThumbnail(Thumbnail thumbnail){
			this.thumbnail=thumbnail;
			
			return this;
		}
		
		public FeedItem build(){
			return new FeedItem(page_id, title, thumbnail);
		}
		
	}
}
