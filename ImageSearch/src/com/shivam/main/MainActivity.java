package com.shivam.main;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.volly.R;
import com.shivam.adapter.ImageAdapter;
import com.shivam.app.AppController;
import com.shivam.data.FeedItem;
import com.shivam.data.Thumbnail;

/**
 * This is the main Activity to search images
 * 
 * */
public class MainActivity extends Activity {

	//List of FeedItems 
	List<FeedItem> feedItems;	
	//Adapter class for ListView
	ImageAdapter listAdapter;	
	
	//Base Link to search Images of height 500px
	private String URL_ITEMS = "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=thumbnail&pithumbsize=500&pilimit=50&generator=prefixsearch&gpssearch=";
	
	ListView listView; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.list);
		feedItems = new ArrayList<FeedItem>();
		listAdapter = new ImageAdapter(this, feedItems);
		listView.setAdapter(listAdapter);

		EditText editText = (EditText) findViewById(R.id.imageSearch);
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				//Get JSON on Text Change in Edit Text
				getJson(s.toString());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	JsonObjectRequest jsonReq;

	/*
	 * This method is used to get JSON Data from Server 
	 * @param searcher - searched string
	 * */
	private void getJson(String searcher) {
		
		searcher = searcher.replaceAll(" ", "%20");	//Replace 

		String url = URL_ITEMS + searcher;	//Create complete URL by adding search Term
		
		//Clear Current list of items and notify to adapter to clear data on UI
		//Also clear current pending requests
		//Also cancel current JSON requests
		for(FeedItem item :feedItems){
			if(item.getThumbnail()!=null)
				AppController.getInstance().cancelPendingRequest(item.getThumbnail().getSource());
		}
		feedItems.clear();
		listAdapter.notifyDataSetChanged();
		if (jsonReq != null)
			jsonReq.cancel();
		
		//Check if image is available in Cache
		Cache cache = AppController.getRequestQueue().getCache();
		Entry entry = cache.get(url);
		
		//If available, get the image form Cache
		if (entry != null) {
			try {
				String data = new String(entry.data, "UTF-8");
				try {
					//Parse JSON Data
					parseJsonFeed(new JSONObject(data));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} 
		
		//If not available, then get data from Server
		else {
			
			jsonReq = new JsonObjectRequest(url, null,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							if (response != null) {
								//Parse JSON Data
								parseJsonFeed(response);
							}

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError response) {
							Toast.makeText(getApplicationContext(),
									"Error" + response.getMessage(),
									Toast.LENGTH_LONG).show();

						}
					});

			
			
			jsonReq.setRetryPolicy(new DefaultRetryPolicy(5000,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

			//Add this request to Request queue with URL as TAG
			AppController.getInstance().addToRequestQueue(jsonReq, url);
		}
	}

	/*
	 * This method is used to parse JSON Data 
	 * @param : response - JSONObject returned from Server
	 * */
	private void parseJsonFeed(JSONObject response) {
		try {
			
			//Get 'pages' object from response data 
			response = response.getJSONObject("query");
			response = response.getJSONObject("pages");
			Iterator<String> keys = response.keys();
			JSONObject temp;
			
			//Get all searched items 
			while (keys.hasNext()) {
				temp = response.getJSONObject(keys.next());
				
				String id = temp.getString("pageid");
				String title = temp.getString("title");
				
				JSONObject jsonObj = temp.isNull("thumbnail") ? null : temp
						.getJSONObject("thumbnail");
				
				Thumbnail thumbnail = null == jsonObj ? null : new Thumbnail(
						jsonObj.getString("source"), jsonObj.getInt("width"),
						jsonObj.getInt("height"));
				
				//Save Individual entry as FeedItem Object
				FeedItem item = new FeedItem.FeedItemBuilder(id, title)
						.setThumbnail(thumbnail).build();

				//Add every item in list
				feedItems.add(item);
			}
			
			//Animation to slide up searched data
			Animation anim = AnimationUtils.loadAnimation(MainActivity.this,
					R.anim.slide_right_in);
			anim.setDuration(500);

			// listView.setAlpha(0f);
			listView.setAnimation(anim);
			
			// notify data changes to list adapter
			listAdapter.notifyDataSetChanged();

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}