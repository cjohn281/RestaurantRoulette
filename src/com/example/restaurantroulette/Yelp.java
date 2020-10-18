/********************************************************************************************************
* Final Project                                                                                         *
*                                                                                                       *
* Yelp.java                                                                                             *
*                                                                                                       *
* Christopher Johnson                                                                                   *
* Thomas Pullen                                                                                         *
*                                                                                                       *
*********************************************************************************************************/

package com.example.restaurantroulette;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class Yelp {
	
	// OAuth credentials
	OAuthService service;
	Token accessToken;
	
	public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
		this.service = new ServiceBuilder().provider(YelpApi2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
		this.accessToken = new Token(token, tokenSecret);
	}
	
	public String search(String categoryFilter, double latitude, double longitude, int radius) {
		//Log.d("RunTest", "during search: Lat: " + latitude + ", Long: " + longitude);//////////////////////////////////////////////////////////////////////////////////////////////////////////
		OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
		request.addQuerystringParameter("category_filter", categoryFilter);
		request.addQuerystringParameter("radius_filter", "" + radius);
		request.addQuerystringParameter("ll", latitude + "," + longitude);
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
		
		return response.getBody();
	}
	
	public String favoritesSearch(String term, double latitude, double longitude, int radius) {
		OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
		request.addQuerystringParameter("term", term);
		request.addQuerystringParameter("radius_filter", "" + radius);
		request.addQuerystringParameter("ll", latitude + "," + longitude);
		request.addQuerystringParameter("limit", "5");
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
		
		return response.getBody();
	}

}
