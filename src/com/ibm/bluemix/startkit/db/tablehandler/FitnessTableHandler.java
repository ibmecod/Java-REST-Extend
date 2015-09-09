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

public class FitnessTableHandler {

	
	public static int _idColumn   = 1;
	public static int _nameColumn = 2;
	
	public static String _fitnessObjectKey = "name";
	public static String _fitnessArrayKey  = "fitnessRoutines";
	
	
	/*
	 * Constructor : Input ALL DB related parameters
	 * 
	 */
	public FitnessTableHandler( ) {
		
	}
	
	public static int getFitnessID( String fitness, DBHandler dbService ) throws SQLException{
		
		String queryString = "SELECT fitness.id FROM fitness WHERE (name='" ;
		       queryString += fitness;
		       queryString += "')";
		       
        ResultSet fitnessSet = dbService.runSelectQuery( queryString );
        
        if( fitnessSet.next( ) ){
        	
        	return fitnessSet.getInt( 1 );
        }
        else{
        	return -1;
        }
        
	}
	
	
	public static String getFitnessName( String fitnessID, DBHandler dbService ) throws SQLException{
		
		String queryString = "SELECT fitness.name FROM fitness WHERE (id=" ;
		       queryString += fitnessID;
		       queryString += ")";
		       
        ResultSet fitnessSet = dbService.runSelectQuery( queryString );
        
        if( fitnessSet.next( ) ){
        	
        	return fitnessSet.getString( 1 );
        }
        else{
        	return null;
        }
        
	}
	
	public static JSONObject getAllFitnessRoutines( DBHandler dbService ) throws SQLException, JSONException{
		
		JSONObject fitnessRoutines = new JSONObject();
		
		JSONArray fitnessRoutineArray = new JSONArray();
		
		String queryString = "SELECT fitness.name from fitness";
		ResultSet fitnessSet = dbService.runSelectQuery( queryString );
		
		while( fitnessSet.next( ) ){
			
			JSONObject fitness = new JSONObject();
			fitness.put( _fitnessObjectKey , fitnessSet.getString( 1 ) );
			
			fitnessRoutineArray.put( fitness );
		}
		
		fitnessRoutines.put( _fitnessArrayKey, fitnessRoutineArray );
		
		return fitnessRoutines;
	
	}
	
}
