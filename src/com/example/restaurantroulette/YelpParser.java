/********************************************************************************************************
* Final Project                                                                                         *
*                                                                                                       *
* YelpParser.java                                                                                       *
*                                                                                                       *
* Christopher Johnson                                                                                   *
* Thomas Pullen                                                                                         *
*                                                                                                       *
*********************************************************************************************************/

package com.example.restaurantroulette;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class YelpParser {

	private String yelp_response;
	public ArrayList<Restaurant> restaurantArray = new ArrayList<Restaurant>();
	JSONArray jArray = null;
	
	private static final String TAG_BUSINESS = "businesses";
	private static final String TAG_NAME = "name";
	private static final String TAG_ID = "id";
	private static final String TAG_LOCATION = "location";
	private static final String TAG_ADDRESS = "display_address";
	private static final String TAG_PHONE = "display_phone";
	private static final String TAG_RATING = "rating_img_url_large";
	private static final String TAG_IMAGE = "image_url";
	private static final String TAG_COORDINATE = "coordinate";
	private static final String TAG_LATITUDE = "latitude";
	private static final String TAG_LONGITUDE = "longitude";
	     
    public void setResponse(String response){
    	yelp_response = response;
    }
	     
    public String getResponse(){
    	return yelp_response;
    }
	     
    public ArrayList<Restaurant> parseRestaurants() {
        
        try {
        	
        	//Log.d("RunTest", yelp_response);
        	
        	JSONObject json = new JSONObject(yelp_response);
        	
        	jArray = json.getJSONArray(TAG_BUSINESS);
        	
        	for (int i = 0; i < jArray.length(); i++) {
				JSONObject r = jArray.getJSONObject(i);
				restaurantArray.add(new Restaurant());

				restaurantArray.get(i).setId(r.optString(TAG_ID));
				restaurantArray.get(i).setName(r.optString(TAG_NAME));
				restaurantArray.get(i).setPhone(r.optString(TAG_PHONE));
				restaurantArray.get(i).setRating(r.optString(TAG_RATING));
				restaurantArray.get(i).setImage(r.optString(TAG_IMAGE));
				if (restaurantArray.get(i).getPhone().equalsIgnoreCase(""))
					restaurantArray.get(i).setPhone("Phone number unavailable");
				
				JSONObject l = r.getJSONObject(TAG_LOCATION);
				restaurantArray.get(i).setAddress(l.optString(TAG_ADDRESS));
				if (restaurantArray.get(i).getAddress().equalsIgnoreCase(""))
					restaurantArray.get(i).setAddress("Address Unavailable");
				
				JSONObject c = l.getJSONObject(TAG_COORDINATE);
				restaurantArray.get(i).setLatitude(c.optString(TAG_LATITUDE));
				restaurantArray.get(i).setLongitude(c.optString(TAG_LONGITUDE));
				
        	}
        } catch (JSONException je) {
			Log.d("Exceptions", "JSONException");/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			je.printStackTrace();
		}
        
        return restaurantArray;
    }
    
    public ArrayList<Restaurant> parseRestaurantsLimited() {
        
        try {
        	
        	//Log.d("RunTest", yelp_response);
        	
        	JSONObject json = new JSONObject(yelp_response);
        	
        	jArray = json.getJSONArray(TAG_BUSINESS);
        	
        	for (int i = 0; i < 10; i++) {
				JSONObject r = jArray.getJSONObject(i);
				restaurantArray.add(new Restaurant());

				restaurantArray.get(i).setId(r.optString(TAG_ID));
				restaurantArray.get(i).setName(r.optString(TAG_NAME));
				restaurantArray.get(i).setPhone(r.optString(TAG_PHONE));
				restaurantArray.get(i).setRating(r.optString(TAG_RATING));
				restaurantArray.get(i).setImage(r.optString(TAG_IMAGE));
				if (restaurantArray.get(i).getPhone().equalsIgnoreCase(""))
					restaurantArray.get(i).setPhone("Phone number unavailable");
				
				JSONObject l = r.getJSONObject(TAG_LOCATION);
				restaurantArray.get(i).setAddress(l.optString(TAG_ADDRESS));
				if (restaurantArray.get(i).getAddress().equalsIgnoreCase(""))
					restaurantArray.get(i).setAddress("Address Unavailable");
				
				JSONObject c = l.getJSONObject(TAG_COORDINATE);
				restaurantArray.get(i).setLatitude(c.optString(TAG_LATITUDE));
				restaurantArray.get(i).setLongitude(c.optString(TAG_LONGITUDE));
				
        	}
        } catch (JSONException je) {
			Log.d("Exceptions", "JSONException");/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			je.printStackTrace();
		}
        
        return restaurantArray;
    }
}