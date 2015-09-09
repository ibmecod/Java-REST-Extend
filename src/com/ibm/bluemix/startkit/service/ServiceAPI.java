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
package com.ibm.bluemix.startkit.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;




import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.bluemix.startkit.db.DBHandler;
import com.ibm.bluemix.startkit.db.object.UserProfile;
import com.ibm.bluemix.startkit.db.tablehandler.EthnicityTableHandler;
import com.ibm.bluemix.startkit.db.tablehandler.FitnessTableHandler;
import com.ibm.bluemix.startkit.db.tablehandler.UserProfileTableHandler;
import com.ibm.bluemix.startkit.service.tasks.SearchTipsTask;
import com.ibm.bluemix.startkit.services.ServiceDiscovery;


@Path("/service")
public class ServiceAPI {


	ServiceDiscovery seHandler= new ServiceDiscovery();
	public String _dbHost = seHandler.getDBHostName();
	public String _dbPort = seHandler.getDBPort();
	public String _dbUser = seHandler.getDBUser();
	public String _dbPassword =seHandler.getDBPassword();
	public String _dbName = seHandler.getDBName();

	private String _ceBaseURL = seHandler.getCEURL();
	private String _ceUsername = seHandler.getCEUserName();
	private String _cePassword = seHandler.getCEPassword();

	public String _qaBaseURL = seHandler.getQAURL();
	public String _qaUsername = seHandler.getQAUserName();
	public String _qaPassword = seHandler.getQAPassword();


	@GET
	public String getInformation() {
		// 'VCAP_APPLICATION' is in JSON format, it contains useful information
		// about a deployed application
		// String envApp = System.getenv("VCAP_APPLICATION");

		// 'VCAP_SERVICES' contains all the credentials of services bound to
		// this application.
		// String envServices = System.getenv("VCAP_SERVICES");
		// JSONObject sysEnv = new JSONObject(System.getenv());

		String dbhost = "Hi World -" +this._qaBaseURL;
		return dbhost;

	}

	@Path("/login")
	@POST
	public String checkLogin(String creds) {
		DBHandler dbService = null;
		UserProfileTableHandler userTableHandler = null;
		try {

			dbService = new DBHandler(_dbHost, _dbPort, _dbUser, _dbPassword,
					_dbName);
			userTableHandler = new UserProfileTableHandler();

			JSONObject credentials = new JSONObject(creds);

			String userID = credentials.getString("user_id");
			String password = credentials.getString("password");

			boolean checkResult = userTableHandler.isRegistered(userID,
					password, dbService);

			dbService.closeConnection();

			if (checkResult) {

				return "Successful";
			} else {

				return "Failed";
			}

		} catch (ClassNotFoundException | SQLException | JSONException e) {
			// TODO Auto-generated catch block
			e.getStackTrace();

			if (dbService != null) {
				try {
					dbService.closeConnection();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			return "Failed";

		}
	}

	@Path("/registernew")
	@POST
	public String registernew(String registrationDetail) {

		DBHandler dbService = null;
		UserProfileTableHandler userTableHandler = null;
		try {

			dbService = new DBHandler(_dbHost, _dbPort, _dbUser, _dbPassword, _dbName);
			userTableHandler = new UserProfileTableHandler();

			JSONObject registration = new JSONObject(registrationDetail);
			String userID = registration.getString("user_id");
			String password = registration.getString("password");
			String name = registration.getString("name");
			int age = registration.getInt("age");
			String zip = registration.getString("zip");
			String ethnicity = registration.getString("ethnicity");
			String fitness = registration.getString("fitness");
			String profession = registration.getString("profession");
			String interests = registration.getString("interests");

			boolean checkResult = userTableHandler.isRegistered(userID,
					dbService);

			if (checkResult) {

				if (dbService != null) {
					try {
						dbService.closeConnection();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				return "Successful";
			}

			// System.out.println( umo.toString() );

			UserProfile newUser = new UserProfile(userID, name, age, zip,
					ethnicity, fitness, profession, interests, password);

			boolean insertResult = userTableHandler.insertUser(newUser,
					dbService);

			if (!insertResult) {
				return "Patient insertion failed";
			}

			dbService.closeConnection();

			if (insertResult) {

				return "Successful";
			} else {
				return "Personality insertion failed";
			}

		}

		catch (ClassNotFoundException | SQLException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (dbService != null) {
				try {
					dbService.closeConnection();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			return "Failed";
		}

	}

	@Path("/getethnicities")
	@POST
	public String getEthnicities() {
		DBHandler dbService = null;
		String ethinicities = null;

		try {
			dbService = new DBHandler(_dbHost, _dbPort, _dbUser, _dbPassword,_dbName);

			org.json.JSONObject ethnicities = EthnicityTableHandler
					.getAllEthnicities(dbService);

			dbService.closeConnection();

			ethinicities = ethnicities.toString();

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return "Failed";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ethinicities;
	}

	@Path("/getfitnessroutines")
	@POST
	public String getFitnessRoutines() {
		DBHandler dbService = null;
		String routines = "";

		try {
			dbService = new DBHandler(_dbHost, _dbPort, _dbUser, _dbPassword,_dbName);

			org.json.JSONObject fitnessRoutines = FitnessTableHandler
					.getAllFitnessRoutines(dbService);

			dbService.closeConnection();

			routines = fitnessRoutines.toString();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return "Failed";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return routines;
	}

	@Path("/getuserprofile")
	@POST
	public String getUserProfile(String id) {

		DBHandler dbService = null;
		UserProfileTableHandler userTableHandler = null;
		String prof = "";

		try {
			dbService = new DBHandler(_dbHost, _dbPort, _dbUser, _dbPassword,_dbName);
			userTableHandler = new UserProfileTableHandler();

			org.json.JSONObject userIDObject = new org.json.JSONObject(id);
			String userID = userIDObject.getString("user_id");

			UserProfile user = userTableHandler.retreiveDetail(userID,
					dbService);

			if (user == null) {

				if (dbService != null) {
					try {
						dbService.closeConnection();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				return "Failed";
			}

			org.json.JSONObject profileObject = user.createProfileJSON();

			dbService.closeConnection();

			prof = profileObject.toString();

		} catch (ClassNotFoundException | SQLException sqle) {
			// TODO Auto-generated catch block
			sqle.printStackTrace();
			if (dbService != null) {
				try {
					dbService.closeConnection();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			return "Failed";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prof;

	}

	@Path("/updateuserprofile")
	@POST
	public String updateUserProfile(String profileString) {

		DBHandler dbService = null;
		UserProfileTableHandler userTableHandler = null;
		String profhandle = "";

		try {
			dbService = new DBHandler(_dbHost, _dbPort, _dbUser, _dbPassword,_dbName);
			userTableHandler = new UserProfileTableHandler();

			org.json.JSONObject userProfileObject = new org.json.JSONObject(
					profileString);

			String name = userProfileObject.getString(UserProfile._nameKey);
			int age = userProfileObject.getInt(UserProfile._ageKey);
			String zip = userProfileObject.getString(UserProfile._zipKey);
			String profession = userProfileObject
					.getString(UserProfile._professionKey);
			String interests = userProfileObject
					.getString(UserProfile._interestsKey);
			String ethnicity = userProfileObject
					.getString(UserProfile._ethnicityKey);
			String fitness = userProfileObject
					.getString(UserProfile._fitnessKey);
			String userID = userProfileObject.getString(UserProfile._userIDKey);
			String password = userProfileObject
					.getString(UserProfile._passwordKey);

			boolean updateResult = userTableHandler.udpateDetail(userID, name,
																age, zip, ethnicity, fitness, profession, interests,
																password, dbService);

			if (!updateResult) {

				if (dbService != null) {
					try {
						dbService.closeConnection();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				profhandle = "Failed";
			} else {

				profhandle = "Successful";
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (dbService != null) {
				try {
					dbService.closeConnection();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			return "Failed";
		} catch (org.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return profhandle;
	}

	@Path("/searchtips")
	@POST
	public String searchTips( String params ){
		
		org.json.JSONObject paramObj = null;
		
		SearchTipsTask csd = new SearchTipsTask(  );
		
		csd.createCEService( _ceBaseURL, _ceUsername, _cePassword );
		csd.createQAService( _qaBaseURL, _qaUsername, _qaPassword );
		
		
		try{
			
			
			paramObj = new JSONObject( params );
			
			String keyword      = paramObj.getString( "keyword" );
			String searchResult = csd.runSteps( keyword );
			
			//System.out.println( searchResult );
			return searchResult;
			
		}
		catch( JSONException | SQLException | URISyntaxException | IOException | InterruptedException e ){
			
			e.printStackTrace();
			System.out.println(e.getMessage());

			return "Failed";
		}
	}

}