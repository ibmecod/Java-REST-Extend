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


import com.ibm.bluemix.startkit.db.DBHandler;
import com.ibm.bluemix.startkit.db.object.UserProfile;

public class UserProfileTableHandler {

	//Rajib: strangely the column index starts from 1 and NOT from 0
		public static int _userIDColumn     = 1;
		public static int _nameColumn       = 2;
		public static int _ageColumn        = 3;
		public static int _zipColumn        = 4;
		public static int _ethnicityColumn  = 5;
		public static int _fitnessColumn    = 6;
		public static int _professionColumn = 7;
		public static int _interestColumn   = 8;
		public static int _passwordColumn   = 9;
		
		
		
		/*
		 * Constructor : Input ALL DB related parameters
		 * 
		 */
		public UserProfileTableHandler( ) {
			
		}
		
		/*
		 * 
		 * Insert a new user; Does not do a checking on its own;
		 * Relies on the DB to reject a new entry if email address matches
		 */
		public boolean insertUser( UserProfile newUser, DBHandler dbService ){
			
			boolean isAlreadyRegistered = isRegistered( newUser.getID(), dbService );
			if( isAlreadyRegistered ){
				return true;
			}
			
			int ethnicityID;
			int fitnessID;
			try {
				ethnicityID = EthnicityTableHandler.getEthnicityID( newUser.getEthnicity()     , dbService );
				fitnessID   = FitnessTableHandler.getFitnessID     ( newUser.getFitnessRoutine(), dbService );
				
				if( ethnicityID == -1 || fitnessID == -1 ){
					return false;
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				return false;
			}
			
			
			
			String queryString = "INSERT INTO users (user_id, password, name, age, zip_code, fk_ethnicity, fk_fitness, profession, interests) VALUES (";
			       queryString = queryString + "'" + newUser.getID()      + "'" + "," ;
			       queryString = queryString + "'" + newUser.getPassword()       + "'" + "," ;
			       queryString = queryString + "'" + newUser.getName()           + "'" + "," ;
			       queryString = queryString +       newUser.getAge()                  + "," ;
			       queryString = queryString + "'" + newUser.getZip()            + "'" + "," ;
			       queryString = queryString +       ethnicityID                       + "," ;
			       queryString = queryString +       fitnessID                         + "," ;
			       queryString = queryString + "'" + newUser.getProfession()     + "'" + "," ;
			       queryString = queryString + "'" + newUser.getInterests()      + "'"       ;
			       queryString = queryString + ")";
			
			//System.out.println( queryString );
			return dbService.runInsertQuery( queryString );
		}
		
		/*
		 * 
		 * Given a user_id checks if the user is registered or not;
		 * Returns TRUE if registered, otherwise FALSE
		 * 
		 * Returns FALSE on any exception too !!!
		 */
		public boolean isRegistered( String userID, DBHandler dbService ){
			
			String queryString = "SELECT users.name FROM users WHERE (";
				   queryString = queryString + "user_id='" + userID + "'";
				   queryString = queryString + ")";
				   
			try {
				ResultSet userList = dbService.runSelectQuery( queryString );
				
				if( userList.next() ){
					
					return true;
				}
				else{
					
					return false;
				}
			} 
			catch ( SQLException e ) {
				e.printStackTrace();
				return false;
			}
			
		}
		
		
		
		/*
		 * 
		 * Given a user_id and password checks if the user is registered or not;
		 * Returns TRUE if registered, otherwise FALSE
		 * 
		 * Returns FALSE on any exception too !!!
		 */
		public boolean isRegistered( String userID, String password, DBHandler dbService ){
			
			String queryString = "SELECT users.name FROM users WHERE (";
				   queryString = queryString + "user_id='" + userID + "'";
				   queryString = queryString + " AND "   ;
				   queryString = queryString + "password='" + password + "'";
				   queryString = queryString + ")";
				   
		    
		    
			try {
				ResultSet userList = dbService.runSelectQuery( queryString );
				
				if( userList.next() ){
					return true;
				}
				else{
					return false;
				}
			} 
			catch ( SQLException e ) {
				e.printStackTrace();
				return false;
			}
			
		}
		
		/*
		 * 
		 * Retrieves detail of a patient from the DB given the email address
		 * 
		 * Returns Patient object is successful or null if otherwise
		 */
		public UserProfile retreiveDetail( String userID, DBHandler dbService ){
			
			String queryString = "SELECT users.user_id, users.name, users.age, users.zip_code, "
					           + "ethnicity.name as ethnicity, fitness.name as fitness, users.profession, "
					           + "users.interests, users.password "
					           + "FROM users as users INNER JOIN ethnicity ON "
					           + "ethnicity.id=users.fk_ethnicity INNER JOIN fitness ON "
					           + "fitness.id=users.fk_fitness WHERE users.user_id='"
					           + userID + "'";
			
			
		    ResultSet userResultSet;
			try {
				userResultSet = dbService.runSelectQuery( queryString );
			    if( userResultSet.next() ){
			    	
			    	UserProfile u = new UserProfile( userResultSet.getString( UserProfileTableHandler._userIDColumn     ),
			    									 userResultSet.getString( UserProfileTableHandler._nameColumn       ),
			    									 userResultSet.getInt   ( UserProfileTableHandler._ageColumn        ),
			    									 userResultSet.getString( UserProfileTableHandler._zipColumn        ),
										    		 userResultSet.getString( UserProfileTableHandler._ethnicityColumn  ),
										    		 userResultSet.getString( UserProfileTableHandler._fitnessColumn    ),
										    		 userResultSet.getString( UserProfileTableHandler._professionColumn ),
										    		 userResultSet.getString( UserProfileTableHandler._interestColumn   ),
										    		 userResultSet.getString( UserProfileTableHandler._passwordColumn   ) );
			    				    	return u;
			    }
			    else{
			    	
			    	return null;
			    }
		    
			} catch (SQLException e) {

				e.printStackTrace();
				return null;
			}
		}
		
		/*
		 * Updates the user profile;
		 * 
		 */
		public boolean udpateDetail( String userID, String name, int age, String zip,
				                     String ethnicity, String fitness, String profession,
				                     String interests, String password, DBHandler dbService ) throws SQLException{
			
			int ethnicityID = EthnicityTableHandler.getEthnicityID( ethnicity, dbService );
			int fitnessID  = FitnessTableHandler.getFitnessID    ( fitness, dbService   );
			
			
			String  queryString =  "UPDATE users SET ";
					queryString += "name='"          + name        + "',";
					queryString += "age="            + age         + "," ;
					queryString += "zip_code='"      + zip         + "',";
					queryString += "fk_ethnicity="   + ethnicityID + "," ;
					queryString += "fk_fitness="     + fitnessID   + "," ;
					queryString += "profession='"    + profession  + "',";
					queryString += "interests='"     + interests   + "',";
					queryString += "password='"      + password    + "' ";
					queryString += "WHERE user_id='" + userID + "'";
			       
		    return dbService.runInsertQuery( queryString );
			       

		}
		
}
