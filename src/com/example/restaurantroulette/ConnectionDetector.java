/********************************************************************************************************
* Final Project                                                                                         *
*                                                                                                       *
* ConnectionDetector.java                                                                               *
*                                                                                                       *
* Christopher Johnson                                                                                   *
* Thomas Pullen                                                                                         *
*                                                                                                       *
*********************************************************************************************************/

package com.example.restaurantroulette;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
	
	private Context _context;
	
	public ConnectionDetector(Context context) {
		this._context = context;
	}
	
	// Retrieves network info, determines if connection is established
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null) {
			if (info.isConnected()) {
				return true;
			} else
				return false;
		} else
			return false;
	}
}