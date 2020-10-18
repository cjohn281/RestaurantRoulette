/********************************************************************************************************
* Final Project                                                                                         *
*                                                                                                       *
* FavoritesActivity.java                                                                                *
*                                                                                                       *
* Christopher Johnson                                                                                   *
* Thomas Pullen                                                                                         *
*                                                                                                       *
*********************************************************************************************************/

package com.example.restaurantroulette;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FavoritesActivity extends Activity {
	
	// OAuth credentials
	final String consumerKey = "5coToqMVhj-qVrskHE8ysQ";
	final String consumerSecret = "GqO4354mnSA1S5nkI2rxu1a8sDA";
	final String token = "fuEjllH5JjceEyBJU2XdBAVFgeffM5aW";
	final String tokenSecret = "FR8ZQFq8WiR154KXq-aescR3kD8";
	
	ListView lv;
	double[] coords;
	ArrayList<Restaurant> favorites = new ArrayList<Restaurant>();
	ArrayList<Restaurant> results = null;
	Restaurant newFavorite = new Restaurant();
	ViewAdapter adapter;
	String userInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        
        Bundle b = getIntent().getExtras();
        coords = b.getDoubleArray("coord");
        
        Button backBtn = (Button) findViewById(R.id.favoritesBackButton);
        Button addBtn = (Button) findViewById(R.id.findFavoritesButton);
        
        backBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		finish();
        	}
        });
        
        // Searches for Restaurants to add to favorites
        addBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		
        		final Dialog dialog = new Dialog(FavoritesActivity.this);
        		Window window = dialog.getWindow();
        		window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        		dialog.setCancelable(true);
        		dialog.setCanceledOnTouchOutside(true);
        		dialog.setContentView(R.layout.search_dialog);
        		dialog.setTitle("Search for Restaurants");
        		
        		final EditText input = (EditText) dialog.findViewById(R.id.inputEditText);
        		Button search = (Button) dialog.findViewById(R.id.dialogSearch);
        		Button cancel = (Button) dialog.findViewById(R.id.dialogCancel);
        		
        		search.setOnClickListener(new OnClickListener() {
        			public void onClick(View view) {
        				Log.d("RunTest", input.getEditableText().toString());
        				userInput = input.getEditableText().toString();
        				dialog.dismiss();
        				new GetResults().execute();
        			}
        		});
        		
        		cancel.setOnClickListener(new OnClickListener() {
        			public void onClick(View view) {
        				dialog.dismiss();
        			}
        		});
        		
        		dialog.show();
        	}
        });
		
    }

    // Loads favorites list from database
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		favorites.clear();
		FavoritesDB table = new FavoritesDB(FavoritesActivity.this);
		table.open();
		if(table.isEmpty()) {
			Toast.makeText(FavoritesActivity.this, "You have not added any favorites yet.", Toast.LENGTH_SHORT).show();
			table.close();
		} else {
			table.close();
			favoriteList();
			
			lv = (ListView) findViewById(R.id.listview);
			adapter = new ViewAdapter(this, R.id.listview, favorites);
			lv.setAdapter(adapter);
			
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(FavoritesActivity.this, ResultActivity.class);
					intent.putExtra("restaurant", favorites.get(position));
					startActivity(intent);
				} 
			});
		}
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_favorites, menu);
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
    
    // takes restaurant data from database and applies it to arraylist of restaurants
    private void favoriteList() {
		// TODO Auto-generated method stub
		FavoritesDB info = new FavoritesDB(FavoritesActivity.this);
    	info.open();
    	int i = 0;
    	for (long l = 1; l < info.countEntries() + 1; l++) {
    		if (!info.getID(l).equalsIgnoreCase("empty")) {
    			favorites.add(new Restaurant());
    			favorites.get(i).setId(info.getID(l));
    			favorites.get(i).setName(info.getName(l));
    			favorites.get(i).setAddress(info.getAddress(l));
    			favorites.get(i).setPhone(info.getPhone(l));
   				favorites.get(i).setRating(info.getRating(l));
   				favorites.get(i).setImage(info.getImage(l));
   				favorites.get(i).setLatitude(info.getLatitude(l));
   				favorites.get(i).setLongitude(info.getLongitude(l));
   				i++;
    		}
    	}
    	info.close();
	}
    
    // Get search results
    public class GetResults extends AsyncTask<String, Void, ArrayList<Restaurant>> {
    	
    	ProgressDialog pd;

    	@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
    		pd = new ProgressDialog(FavoritesActivity.this);
    		pd.setCancelable(false);
    		pd.setTitle("Searching");
    		pd.setMessage("Getting results...");
    		pd.show();
		}

		@Override
		protected ArrayList<Restaurant> doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
			String response = yelp.favoritesSearch(userInput, coords[0], coords[1], 40000);
			
			YelpParser parser = new YelpParser();
			parser.setResponse(response);
			
			 if (response.contains("\"total\": 0")) {
				 pd.dismiss();
				 AlertDialog.Builder builder = new AlertDialog.Builder(FavoritesActivity.this);
				 builder.setTitle("No results found");
				 builder.setMessage("No matches were found...");
				 builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
			    	return null;
			 } else {
				 return parser.parseRestaurantsLimited();
			 }
		}
		
		@Override
		protected void onPostExecute(ArrayList<Restaurant> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if (result != null)
				results = result;
			
			Intent intent = new Intent(FavoritesActivity.this, SearchResultsActivity.class);
			
			intent.putExtra("results", results);
						
			//result.get(0).toLogString();
			
			pd.dismiss();
			startActivity(intent);
		}
		
    }
}
