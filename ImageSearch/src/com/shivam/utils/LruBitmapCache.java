package com.shivam.utils;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * LRU cache to cache Images returned form Server
 * */
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageCache{

	public static int getDefaultLruCacheSize(){
		final int maxMemory=(int)(Runtime.getRuntime().maxMemory()/1024);
		final int cacheSize=maxMemory/4;
		return cacheSize;
	} 
	
	public LruBitmapCache(){
		this(getDefaultLruCacheSize());
	}
	
	public LruBitmapCache(int maxSize) {
		super(maxSize);
	}

	@Override
	public Bitmap getBitmap(String url) {
		return get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url,bitmap);
		
	}

}
