/********************************************************************************************************
* Final Project                                                                                         *
*                                                                                                       *
* MyItemizedOverlay.java                                                                                *
*                                                                                                       *
* Christopher Johnson                                                                                   *
* Thomas Pullen                                                                                         *
*                                                                                                       *
*********************************************************************************************************/

package com.example.restaurantroulette;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem>{
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	Context mContext;

	public MyItemizedOverlay(Drawable marker) {
		super(boundCenterBottom(marker));
		// TODO Auto-generated constructor stub
	}
	
	public MyItemizedOverlay(Drawable marker, Context context) {
		super(boundCenterBottom(marker));
		mContext = context;
	}
	
	public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        populate();
    }

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}

}
