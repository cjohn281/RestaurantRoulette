/********************************************************************************************************
* Final Project                                                                                         *
*                                                                                                       *
* ViewAdapter.java                                                                                      *
*                                                                                                       *
* Christopher Johnson                                                                                   *
* Thomas Pullen                                                                                         *
*                                                                                                       *
*********************************************************************************************************/

package com.example.restaurantroulette;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ViewAdapter extends ArrayAdapter<Restaurant>{
	
	private ArrayList<Restaurant> items;
	private Activity activity;
	
	public ViewAdapter(Activity a, int textViewResourceId, ArrayList<Restaurant> itemList) {
		super(a, textViewResourceId, itemList);
		this.activity = a;
		this.items = itemList;
	}
	
	public static class ViewHolder {
		public TextView name;
		public TextView address;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.restaurant_list, null);
			holder = new ViewHolder();
			holder.name = (TextView) v.findViewById(R.id.restaurantNameText);
			holder.address = (TextView) v.findViewById(R.id.restaurantAddressText);
			v.setTag(holder);
		} else
			holder = (ViewHolder) v.getTag();
		
		final Restaurant restaurantList = items.get(position);	
			if (restaurantList != null) {
				if (restaurantList.getName() != null)
					holder.name.setText(restaurantList.getName());
				if (restaurantList.getAddress() != null)
					holder.address.setText(restaurantList.getAddress());
			}
		return v;
	}
}