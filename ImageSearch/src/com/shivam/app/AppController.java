package com.shivam.app;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.shivam.utils.LruBitmapCache;

import android.app.Application;
import android.text.TextUtils;

/**
 * Singleton class to handle All server Requests
 * */
public class AppController extends Application{
	
	public static final String TAG=AppController.class.getSimpleName();	//Default TAG for AppController 
	static AppController appInstance;
	
	static RequestQueue requestQueue;
	ImageLoader imageLoader;
	
	@Override
	public void onCreate(){
		super.onCreate();
		appInstance=this;
	}
	
	public static synchronized AppController getInstance(){
		return appInstance;
	}
	
	/*
	 * Create Request Queue for JSON data downloading form Server
	 * */
	public static RequestQueue getRequestQueue(){
		if(requestQueue==null){
			requestQueue=Volley.newRequestQueue(appInstance.getApplicationContext());
		}
		return requestQueue;
	}
	
	/*
	 * Create image Loader for image downloading from server and cache
	 * */
	public ImageLoader getImageLoader(){
		getRequestQueue();
		if(imageLoader==null){
			imageLoader=new ImageLoader(requestQueue, new LruBitmapCache());
		}
		return imageLoader;
	}
	
	/*
	 * Add request to request Queue
	 * @param
	 * 		request - Request Object
	 * 		tag	- TAG for current request
	 * */
	public <T> void addToRequestQueue(Request<T> request,String tag){
		
		request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(request);
	}
	
	/*
	 * Add request to request Queue with default TAG
	 * @param
	 * 		request - Request Object
	 * */
	public <T> void addToRequestQueue(Request<T> request){
		request.setTag(TAG);
		getRequestQueue().add(request);
		
	}
	
	/*
	 * Cancel all pending request with this TAG
	 * @param
	 * 		tag	- TAG for current request
	 * */
	public void cancelPendingRequest(Object tag){
		if(requestQueue!=null){
			requestQueue.cancelAll(tag);
		}
	}
}
