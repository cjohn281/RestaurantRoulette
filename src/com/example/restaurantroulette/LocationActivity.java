/********************************************************************************************************
* Final Project                                                                                         *
*                                                                                                       *
* LocationActivity.java                                                                                 *
*                                                                                                       *
* Christopher Johnson                                                                                   *
* Thomas Pullen                                                                                         *
*                                                                                                       *
*********************************************************************************************************/

package com.example.restaurantroulette;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class LocationActivity extends MapActivity {
	
	int lat;
	int lon;
	String routeURL;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        
        Bundle b = getIntent().getExtras();
        Restaurant restaurant = b.getParcelable("toMap");
        
        lat = (int) (restaurant.getLatitude() * 1E6);
        lon = (int) (restaurant.getLongitude() * 1E6);
        
        GeoPoint geopoint = new GeoPoint(lat, lon);
        
        MapView mapview = (MapView) findViewById(R.id.mapview);
        mapview.setBuiltInZoomControls(true);
        mapview.setSatellite(true);
        
        MapController controller = mapview.getController();
        controller.setZoom(16);
        controller.animateTo(geopoint);
        
        List<Overlay> mapOverlays = mapview.getOverlays();
        Drawable drawable1 = this.getResources().getDrawable(R.drawable.dest_marker);
         
        MyItemizedOverlay io1 = new MyItemizedOverlay(drawable1, this);
        
        OverlayItem destination = new OverlayItem(geopoint, restaurant.getName(), restaurant.getAddress());
       
        io1.addOverlay(destination);
        mapOverlays.add(io1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_location, menu);
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

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
