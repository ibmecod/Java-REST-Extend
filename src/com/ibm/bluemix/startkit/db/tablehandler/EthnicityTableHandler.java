/*
 * Copyright 2015 IBM Corp. All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.bluemix.startkit.db.tablehandler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.bluemix.startkit.db.DBHandler;

public class EthnicityTableHandler {
	

	
	public static int _idColumn   = 1;
	public static int _nameColumn = 2;
	
	public static String _ethnicityObjectKey = "name";
	public static String _ethnicityArrayKey  = "ethnicities";
	
	
	
	/*
	 * Constructor : Input ALL DB related parameters
	 * 
	 */
	public EthnicityTableHandler( ) {
		
	}
	
	public static int getEthnicityID( String ethnicity, DBHandler dbService ) throws SQLException{
		
		String queryString = "SELECT ethnicity.id FROM ethnicity WHERE (name='" ;
		       queryString += ethnicity;
		       queryString += "')";
		       
        ResultSet ethnicitySet = dbService.runSelectQuery( queryString );
        
        if( ethnicitySet.next( ) ){
        	
        	return ethnicitySet.getInt( 1 );
        }
        else{
        	return -1;
        }
        
	}
	
	
	public static String getEthnicityName( String ethnicityID, DBHandler dbService ) throws SQLException{
		
		String queryString = "SELECT ethnicity.name FROM ethnicity WHERE (id=" ;
		       queryString += ethnicityID;
		       queryString += ")";
		       
        ResultSet ethnicitySet = dbService.runSelectQuery( queryString );
        
        if( ethnicitySet.next( ) ){
        	
        	return ethnicitySet.getString( 1 );
        }
        else{
        	return null;
        }
        
	}
	
	public static JSONObject getAllEthnicities( DBHandler dbService ) throws SQLException, JSONException{
	
		JSONObject ethnicities = new JSONObject();
		
		JSONArray ethnicityArray = new JSONArray();
		
		String queryString = "SELECT ethnicity.name from ethnicity";
		ResultSet ethnicitySet = dbService.runSelectQuery( queryString );
		
		while( ethnicitySet.next( ) ){
			
			JSONObject ethnicity = new JSONObject();
			ethnicity.put( _ethnicityObjectKey , ethnicitySet.getString( 1 ) );
			
			ethnicityArray.put( ethnicity );
		}
		
		ethnicities.put( _ethnicityArrayKey, ethnicityArray );
		
		return ethnicities;
	
	}
	
}
