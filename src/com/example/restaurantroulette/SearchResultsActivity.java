/********************************************************************************************************
* Final Project                                                                                         *
*                                                                                                       *
* SearchResultsActivity.java                                                                            *
*                                                                                                       *
* Christopher Johnson                                                                                   *
* Thomas Pullen                                                                                         *
*                                                                                                       *
*********************************************************************************************************/

package com.example.restaurantroulette;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class SearchResultsActivity extends Activity {
	
	ArrayList<Restaurant> results = new ArrayList<Restaurant>();
	Restaurant temp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        
        Button backBtn = (Button) findViewById(R.id.resultsBackButton);
        
        backBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		finish();
        	}
        });
        
        Bundle b = getIntent().getExtras();
        
        results = b.getParcelableArrayList("results");
        
        ListView lv = (ListView) findViewById(R.id.resultListview);
        ViewAdapter adapter = new ViewAdapter(this, R.layout.restaurant_list, results);
        lv.setAdapter(adapter);
        
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SearchResultsActivity.this, ResultActivity.class);
				intent.putExtra("restaurant", results.get(position));
				Log.d("RunTest", results.get(position).getLatitude() + "");
				startActivity(intent);
			}
        	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search_results, menu);
        return true;
    }
}
