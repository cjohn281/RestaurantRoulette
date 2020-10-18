/********************************************************************************************************
* Final Project                                                                                         *
*                                                                                                       *
* ResultActivity.java                                                                                   *
*                                                                                                       *
* Christopher Johnson                                                                                   *
* Thomas Pullen                                                                                         *
*                                                                                                       *
*********************************************************************************************************/

package com.example.restaurantroulette;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity {
	
	Restaurant restaurant;
	ImageView image;
	ImageView rating;
	ImageView favorite;
	ProgressBar imageLoader;
	Bitmap bmRating;
	double[] homeCoord;
	boolean isFavorite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        
        Button backBtn = (Button) findViewById(R.id.backButton);
        Button mapBtn = (Button) findViewById(R.id.mapButton);
        TextView name = (TextView) findViewById(R.id.nameText);
        TextView address = (TextView) findViewById(R.id.addressText);
        TextView phone = (TextView) findViewById(R.id.phoneText);
        image = (ImageView) findViewById(R.id.restaurantImage);
        rating = (ImageView) findViewById(R.id.ratingImage);
        favorite = (ImageView) findViewById(R.id.favoritesImage);
        imageLoader = (ProgressBar) findViewById(R.id.ImageProgress);
        
        Bundle b = getIntent().getExtras();
        restaurant = b.getParcelable("restaurant");
        homeCoord = b.getDoubleArray("home");
        
        name.setText(restaurant.getName());
        address.setText(restaurant.getAddress());
        phone.setText(restaurant.getPhone());
        
        FavoritesDB db = new FavoritesDB(ResultActivity.this);
		db.open();
		
        if (db.inFavorites(restaurant.getId())) {
        	favorite.setImageResource(R.drawable.favorited);
        	isFavorite = true;
        }
        else {
        	favorite.setImageResource(R.drawable.notfavorited);
        	isFavorite = false;
        }
        
        db.close();
        
        if (restaurant.getImage().equalsIgnoreCase("")) {
        	image.setImageResource(R.drawable.noimage);
        } else {
        	new GetImage().execute("");
        }
        
        new GetRating().execute("");
        
        backBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		finish();
        	}
        });
        
        favorite.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		
        		String latString = restaurant.getLatitude() + "";
        		String longString = restaurant.getLongitude() + "";
        		
        		FavoritesDB entry = new FavoritesDB(ResultActivity.this);
        		if(isFavorite) {
        			try {
						entry.open();
						long l = entry.getRow(restaurant.getId());
						Log.d("RunTest", l + "");
						entry.deleteEntry(l);
						entry.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						favorite.setImageResource(R.drawable.notfavorited);
						Toast.makeText(ResultActivity.this, "Removed from favorites.", Toast.LENGTH_SHORT).show();
					}
        			
        		} else {
        			boolean working = true;
        			try {
        				entry.open();
						entry.createEntry(restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getPhone(), restaurant.getRating(), restaurant.getImage(), latString, longString);
						Log.d("RunTest", "Entry 0: " + entry.getName(1));
						entry.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						working = false;
						Log.d("RunTest", e.toString());
					} finally {
						if (working) {
							favorite.setImageResource(R.drawable.favorited);
							Toast.makeText(ResultActivity.this, "Added to favorites.", Toast.LENGTH_SHORT).show();
						}
					}
        		}
        		
        	}
        });
        
        mapBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		Intent intent = new Intent(ResultActivity.this, LocationActivity.class);
        		intent.putExtra("toMap", restaurant);
        		intent.putExtra("homeToMap", homeCoord);
        		startActivity(intent);
        	}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_result, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public class GetImage extends AsyncTask<String, Void, Bitmap> {
    	
    	URL imgURL;
    	
    	protected void onPreExecute() {
    		imageLoader.setVisibility(View.VISIBLE);
    	}
    	
		@Override
		protected Bitmap doInBackground(String... posterImageDownload) {	

			Bitmap bmImg = null;
			bmRating = null;
			try {
				imgURL = new URL(restaurant.getImage());
			} catch (MalformedURLException muE) {
				Log.d("Exceptions", "MalformedURLException");
			}
			
			try {
				
				HttpURLConnection conn = (HttpURLConnection)imgURL.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
				bmImg = BitmapFactory.decodeStream(is);
				
			} catch (IOException ioE) {
				Log.d("Exceptions", "IOException");
			}
			return bmImg;
		}
		
		protected void onPostExecute(Bitmap bm) {
			image.setImageBitmap(bm);
			imageLoader.setVisibility(View.INVISIBLE);
		}
    	
    }
    
    public class GetRating extends AsyncTask<String, Void, Bitmap> {
    	
    	URL imgURL;
    	
		@Override
		protected Bitmap doInBackground(String... imageURL) {	

			Bitmap bmImg = null;
			bmRating = null;
			try {
				imgURL = new URL(restaurant.getRating());
			} catch (MalformedURLException muE) {
				Log.d("Exceptions", "MalformedURLException");
			}
			
			try {
				
				HttpURLConnection conn = (HttpURLConnection)imgURL.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
				bmImg = BitmapFactory.decodeStream(is);
				
			} catch (IOException ioE) {
				Log.d("Exceptions", "IOException");
			}
			return bmImg;
		}
		
		protected void onPostExecute(Bitmap bm) {
			rating.setImageBitmap(bm);
		}
    	
    }

}
