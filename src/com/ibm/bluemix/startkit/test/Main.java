package com.ibm.bluemix.startkit.test;

import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.bluemix.startkit.service.ServiceAPI;

public class Main {
	
	public static void main( String[] args ) throws JSONException{
		
		JSONObject credential = new JSONObject();
		credential.put( "user_id", "franky1" );
		credential.put( "password", "user1" );
		
		ServiceAPI sapi= new ServiceAPI();
		//System.out.println( sapi.checkLogin( credential.toString() ) );
		
		
		JSONObject searchParams = new JSONObject();
		searchParams.put( "keyword" , "headache" );
		System.out.println( sapi.searchTips( searchParams.toString() ) );
		
	}

}
