/********************************************************************************************************
* Final Project                                                                                         *
*                                                                                                       *
* Restaurant.java                                                                                       *
*                                                                                                       *
* Christopher Johnson                                                                                   *
* Thomas Pullen                                                                                         *
*                                                                                                       *
*********************************************************************************************************/

package com.example.restaurantroulette;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Restaurant implements Parcelable{
	
	private String id, name, address, phone, rating, image;
	private double latitude, longitude;
	
	public Restaurant() {
		id = null;
		name = null;
		address = null;
		phone = null;
		rating = null;
		image = null;
	}
	
	public Restaurant(Parcel in) {
		this.id = in.readString();
		this.name = in.readString();
		this.address = in.readString();
		this.phone = in.readString();
		this.rating = in.readString();
		this.image = in.readString();
		this.latitude = in.readDouble();
		this.longitude = in.readDouble();
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAddress(String add) {
		add = add.replace("[\"", "");
		add = add.replace("\",\"", ", ");
		this.address = add.replace("\"]", "");
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public void setLatitude(String lat) {
		this.latitude = Double.parseDouble(lat);
	}
	
	public void setLongitude(String lon) {
		this.longitude = Double.parseDouble(lon);
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}
	
	public String getRating() {
		return rating;
	}
	
	public String getImage() {
		return image;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void toLogString() {
		Log.d("RunTest", name + ", " + address + ", " + phone + ", " + rating + ", " + image + ", " + latitude + ", " + longitude);/////////////////////////////////////////////////////////////////////
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flag) {
		// TODO Auto-generated method stub
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(address);
		dest.writeString(phone);
		dest.writeString(rating);
		dest.writeString(image);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
	}
	
	public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {

		@Override
		public Restaurant createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new Restaurant(in);
		}

		@Override
		public Restaurant[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Restaurant[size];
		}
		
	};
}
