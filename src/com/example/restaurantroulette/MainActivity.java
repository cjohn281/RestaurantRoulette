/********************************************************************************************************
* Final Project                                                                                         *
*                                                                                                       *
* MainActivity.java                                                                                     *
*                                                                                                       *
* Christopher Johnson                                                                                   *
* Thomas Pullen                                                                                         *
*                                                                                                       *
*********************************************************************************************************/

package com.example.restaurantroulette;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	int miles;
	int withinDistance;
	int pick;
	double currentLatitude;
	double currentLongitude;
	boolean load = false;
	String category;
	
	Button goBtn;
	Spinner selectionSpin;
	Spinner distanceSpin;
	
	ConnectionDetector cd;
	
	LocationManager locationManager;
	LocationListener locationListener;
	
	Random generator = new Random();
	
	ArrayList<Restaurant> restaurantArray;

	// OAuth credentials
	final String consumerKey = "5coToqMVhj-qVrskHE8ysQ";
	final String consumerSecret = "GqO4354mnSA1S5nkI2rxu1a8sDA";
	final String token = "fuEjllH5JjceEyBJU2XdBAVFgeffM5aW";
	final String tokenSecret = "FR8ZQFq8WiR154KXq-aescR3kD8";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Checks for Internet connection and if found, retrieves location data. 
        checkConnection();
        getCurrentLocation(load);
        
        Button exitBtn = (Button) findViewById(R.id.exitButton);
        Button favoritesBtn = (Button) findViewById(R.id.favoritesButton);
        goBtn = (Button) findViewById(R.id.goButton);
        selectionSpin = (Spinner) findViewById(R.id.choiceSpin);
        distanceSpin = (Spinner) findViewById(R.id.distanceSpin);
        
        // Go button starts query and starts Result activity
        goBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		
        		category = String.valueOf(selectionSpin.getSelectedItem()).toLowerCase();
        		if (category.equalsIgnoreCase("all"))
        			category = "restaurants";
        		else if (category.equalsIgnoreCase("wings"))
        			category = "chicken_wings";
        		else if (category.equalsIgnoreCase("fast food"))
        				category = "hotdogs";
        		else if (category.equalsIgnoreCase("breakfast"))
        			category = "breakfast_brunch";
        		else if (category.equalsIgnoreCase("barbeque"))
        			category = "bbq";
        		
        		miles = distanceSpin.getSelectedItemPosition();
        		
        		//Toast.makeText(MainActivity.this, category, Toast.LENGTH_LONG).show();
        		
        		switch (miles) {
        			case 0:
        				withinDistance = 8047;
        				break;
        			case 1:
        				withinDistance = 24140;
        				break;
        			case 2:
        				withinDistance = 40000;
        				break;
        			}
        		
        		new getRestaurants().execute();
        	}
        });
        
        // Exit button closes application
        exitBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		finish();
        	}
        });
        
        // Favorites button starts Favorites activity
        favoritesBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		
        		double[] coords = {currentLatitude, currentLongitude};
        		
        		Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
        		intent.putExtra("coord", coords);
        		startActivity(intent);
        	}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    // Checks for Internet connection
    public void checkConnection() {
    	cd = new ConnectionDetector(getApplicationContext());
    	if (cd.isOnline()) {
    		load = true;    		
    	} else {
        	try {
        		// If no connection, AlertDialog allows user to try to find connection again or to close application
        		AlertDialog ad = new AlertDialog.Builder(MainActivity.this).create();
        		
        		ad.setTitle("No Internet Connection!");
        		ad.setMessage("You must have an Internet connection to use this app!");
        		ad.setCancelable(false);
        		ad.setButton(AlertDialog.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						checkConnection();
					}
				});
        		
        		ad.setButton(AlertDialog.BUTTON_NEGATIVE, "Quit", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();	
					}
				});
        		
        		ad.show();
        	} catch (Exception e) {
        		Log.d("RunTest", "Show Dialog: " + e.getMessage());///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        	}
        }
    }
    
    // If Internet connection is found, this method retrieves GPS coordinates
    public void getCurrentLocation(Boolean start) {
    	if (start) {
    		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    		locationListener = new MyLocationListener();
    		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 40000000, 10000, locationListener);
    	}
    }
    
    
    public class getRestaurants extends AsyncTask<String, Integer, String> {
    	
    	ProgressDialog pd;
    	
    	protected void onPreExecute() {
    		pd = new ProgressDialog(MainActivity.this);
    		pd.setCancelable(false);
    		pd.setTitle("Searching for nearby restaurants...");
    		pd.setMessage("Looking for a good one...");
    		pd.show();
    	}
    	
		@Override
		protected String doInBackground(String... arg0) {
			
			while (currentLongitude == 0.0) {
				
			}
			
			Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
		    String response = yelp.search(category, currentLatitude, currentLongitude, withinDistance);
		    
		    //Log.d("RunTest", response);//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    
		    YelpParser parser = new YelpParser();
		    parser.setResponse(response);
		    
		    //Log.d("RunTest", response);
		    if (response.contains("\"total\": 0")) {
		    	return "";
		    } else {
		    	restaurantArray = parser.parseRestaurants();
		    	return response;
		    }
		}
		
		protected void onPostExecute(String response) {
			if (!response.equalsIgnoreCase("")) {
				pick = generator.nextInt(restaurantArray.size());
				double[] coord = {currentLatitude, currentLongitude};
				Intent intent = new Intent(MainActivity.this, ResultActivity.class);
				intent.putExtra("restaurant", restaurantArray.get(pick));
				intent.putExtra("home", coord);
				pd.dismiss();
			
				startActivity(intent);
			} else {
				pd.dismiss();
				AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
		    	dialog.setTitle("No Restaurants Found");
		    	dialog.setMessage("No restaurants that match your criteria were found.");
		    	dialog.setCanceledOnTouchOutside(true);
		    	dialog.show();
			}
		}
		
    }
    
    public class MyLocationListener implements LocationListener {

    	@Override
    	public void onProviderDisabled(String provider) {
    		// TODO Auto-generated method stub
    		goBtn.setEnabled(false);
    		Toast.makeText(getApplicationContext(), "You must enable GPS on your device.", Toast.LENGTH_LONG).show();
    	}

    	@Override
    	public void onProviderEnabled(String provider) {
    		// TODO Auto-generated method stub
    		goBtn.setEnabled(true);
    		Toast.makeText(getApplicationContext(), "GPS is enabled.", Toast.LENGTH_SHORT).show();
    	}

    	@Override
    	public void onStatusChanged(String provider, int status, Bundle extras) {
    		// TODO Auto-generated method stub
    		
    	}

    	@Override
    	public void onLocationChanged(Location location) {
    		// TODO Auto-generated method stub
    		currentLatitude = location.getLatitude();
    		currentLongitude = location.getLongitude();
    		//Log.d("RunTest", currentLatitude + "   " + currentLongitude);////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    		locationManager.removeUpdates(locationListener);
    	}

    }
    
}
