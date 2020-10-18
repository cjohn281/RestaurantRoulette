/********************************************************************************************************
* Final Project                                                                                         *
*                                                                                                       *
* FavoritesDB.java                                                                                      *
*                                                                                                       *
* Christopher Johnson                                                                                   *
* Thomas Pullen                                                                                         *
*                                                                                                       *
*********************************************************************************************************/

package com.example.restaurantroulette;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesDB {
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_ID = "restaurantId";
	public static final String KEY_NAME = "restaurantName";
	public static final String KEY_ADDRESS = "restaurantAddress";
	public static final String KEY_PHONE = "restaurantPhone";
	public static final String KEY_RATING = "restaurantRating";
	public static final String KEY_IMAGE = "restaurantImage";
	public static final String KEY_LATITUDE = "restaurantLatitude";
	public static final String KEY_LONGITUDE = "restaurantLongitude";
	
	private static final String DATABASE_NAME = "RestaurantDB";
	private static final String DATABASE_TABLE = "restaurantTable";
	private static final int DATABASE_VERSION = 1;
	
	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	String[] columns = new String[]{ KEY_ROWID, KEY_ID, KEY_NAME, KEY_ADDRESS, KEY_PHONE, KEY_RATING, KEY_IMAGE, KEY_LATITUDE, KEY_LONGITUDE};
	
	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_ID + " TEXT NOT NULL, " +
					KEY_NAME + " TEXT NOT NULL, " +
					KEY_ADDRESS + " TEXT NOT NULL, " +
					KEY_PHONE + " TEXT NOT NULL, " +
					KEY_RATING + " TEXT NOT NULL, " +
					KEY_IMAGE + " TEXT NOT NULL, " +
					KEY_LATITUDE + " TEXT NOT NULL, " +
					KEY_LONGITUDE + " TEXT NOT NULL);"
			);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
		
	}
	
	public FavoritesDB(Context c) {
		ourContext = c;
	}
	
	public FavoritesDB open() {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		ourHelper.close();
	}

	public long createEntry(String id, String name, String address,
			String phone, String rating, String image, String latitude,
			String longitude) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_ID, id);
		cv.put(KEY_NAME, name);
		cv.put(KEY_ADDRESS, address);
		cv.put(KEY_PHONE, phone);
		cv.put(KEY_RATING, rating);
		cv.put(KEY_IMAGE, image);
		cv.put(KEY_LATITUDE, latitude);
		cv.put(KEY_LONGITUDE, longitude);
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}

	public String getID(long l) {
		// TODO Auto-generated method stub
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
			String id = c.getString(1);
			return id;
		}
		return null;
	}

	public String getName(long l) {
		// TODO Auto-generated method stub
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
			String name = c.getString(2);
			return name;
		}
		return null;
	}

	public String getAddress(long l) {
		// TODO Auto-generated method stub
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
			String address = c.getString(3);
			return address;
		}
		return null;
	}

	public String getPhone(long l) {
		// TODO Auto-generated method stub
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
			String phone = c.getString(4);
			return phone;
		}
		return null;
	}

	public String getRating(long l) {
		// TODO Auto-generated method stub
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
			String rating = c.getString(5);
			return rating;
		}
		return null;
	}

	public String getImage(long l) {
		// TODO Auto-generated method stub
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
			String image = c.getString(6);
			return image;
		}
		return null;
	}

	public String getLatitude(long l) {
		// TODO Auto-generated method stub
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
			String latitude = c.getString(7);
			return latitude;
		}
		return null;
	}

	public String getLongitude(long l) {
		// TODO Auto-generated method stub
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
			String longitude = c.getString(8);
			return longitude;
		}
		return null;
	}

	public long countEntries() {
		// TODO Auto-generated method stub
		return DatabaseUtils.queryNumEntries(ourDatabase, DATABASE_TABLE);
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		Cursor mCursor = ourDatabase.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
		Boolean rowExists;

		if (mCursor.moveToFirst())
		{
		   // DO SOMETHING WITH CURSOR
		  rowExists = false;

		} else{
		   // I AM EMPTY
		   rowExists = true;
		}
		return rowExists;
	}

	public boolean inFavorites(String id) {
		// TODO Auto-generated method stub
		int found = 0;
		
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		
		int iID = c.getColumnIndex(KEY_ID);

		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			if (c.getString(iID).equals(id)) {
				found++;
			}
		}
		
		if (found > 0)
			return true;
		else
			return false;
	}
	
	public long getRow(String id) {
		// TODO Auto-generated method stub
		long index = 0;
		
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		
		int rowID = c.getColumnIndex(KEY_ROWID);
		int iID = c.getColumnIndex(KEY_ID);

		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			if (c.getString(iID).equals(id)) {
				String rIndex = c.getString(rowID);
				index = Long.parseLong(rIndex);
			}
		}
		return index;
	}

	public void deleteEntry(long l) {
		// TODO Auto-generated method stub
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_ID, "empty");
		ourDatabase.update(DATABASE_TABLE, cvUpdate, KEY_ROWID + "=" + l, null);
	}
	
}