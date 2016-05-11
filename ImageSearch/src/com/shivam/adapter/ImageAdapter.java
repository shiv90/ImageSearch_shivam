package com.shivam.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.android.volley.toolbox.ImageLoader;
import com.example.volly.R;
import com.shivam.app.AppController;
import com.shivam.data.FeedItem;
import com.shivam.feedView.FeedImageView;

/**
 * This class works as Adapter class for ListView
 * 
 * */
public class ImageAdapter extends BaseAdapter {

	static class ViewHolder {
		FeedImageView icon;

	}

	List<FeedItem> feedItems;
	Activity activity;
	ImageLoader loader = AppController.getInstance().getImageLoader();

	public ImageAdapter(Activity actvity, List<FeedItem> feedItems) {
		this.activity = actvity;
		this.feedItems = feedItems;
	}

	@Override
	public int getViewTypeCount() {
		//2 types of View
		//1. View which holds No Image available 
		//2. Actual image returned by Server
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		//Return view type 
		if (feedItems.get(position).getThumbnail() == null)
			return 0;
		else
			return 1;

	}

	@Override
	public int getCount() {
		return feedItems.size();
	}

	@Override
	public long getItemId(int position) {
		//Identify item with its hashCode
		return feedItems.get(position).hashCode();
	}

	@Override
	public Object getItem(int position) {
		return feedItems.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (loader == null)
			loader = AppController.getInstance().getImageLoader();

		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(
					R.layout.feed_image, parent, false);
			
			//Create View Holder to recycle views
			holder = new ViewHolder();
			holder.icon = (FeedImageView) convertView.findViewById(R.id.image);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
			//reset image URL for every new View
			holder.icon.setImageUrl(null, loader);
		}

		FeedItem item = feedItems.get(position);

		//If some image is available for item
		if (item.getThumbnail() != null) {
			holder.icon.setDefaultImageResId(R.drawable.loading);
			holder.icon.setImageUrl(item.getThumbnail().getSource(), loader);

		} 
		
		//If no image available for item
		else {
			holder.icon.setBackgroundResource(R.drawable.no_image);
			holder.icon.setDefaultImageResId(R.drawable.no_image);
			holder.icon.setImageUrl(null, loader);

		}

		return convertView;
	}

}
