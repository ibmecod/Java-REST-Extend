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

package com.ibm.bluemix.startkit.services;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import com.ibm.nosql.json.api.BasicDBList;
import com.ibm.nosql.json.api.BasicDBObject;
import com.ibm.nosql.json.util.JSON;

public class ServiceDiscovery {

	private Connection _connection;
	public String dbHost = "";
	public String dbPort = "";
	public String dbUser = "";
	public String dbPassword = "";
	public String dbName = "";

	private String ceBaseURL = "";
	private String ceUsername = "";
	private String cePassword = "";

	public String qaBaseURL = "";
	public String qaUsername = "";
	public String qaPassword = "";

	public ServiceDiscovery() {
		processVCAP();
	}

	public ServiceDiscovery(String host, String port, String user,
			String password, String dbName) throws ClassNotFoundException,
			SQLException {

		Class.forName("com.mysql.jdbc.Driver");

		_connection = DriverManager.getConnection("jdbc:mysql://" + host + ":"
				+ port + "/" + dbName, user, password);

	}

	public void processVCAP() {

		String MySQL_Service_Name = "cleardb";
		String WatsonConceptExpansion_Service_Name = "concept_expansion";
		String WatsonQandA_Service_Name = "question_and_answer";

		// VCAP_SERVICES is a system environment variable
		// Parse it to obtain the for DB2 connection info
		String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
		if (VCAP_SERVICES != null) {
			// parse the VCAP JSON structure
			BasicDBObject obj = (BasicDBObject) JSON.parse(VCAP_SERVICES);
			String thekey = null;
			Set<String> keys = obj.keySet();
			// Look for the VCAP key that holds the SQLDB information
			for (String eachkey : keys) {
				// Just in case the service name gets changed to lower case in
				// the future, use toUpperCase
				if (eachkey.contains(MySQL_Service_Name)) {
					thekey = eachkey;
					getDBConnectionParams(thekey, obj);
				} else if (eachkey
						.contains(WatsonConceptExpansion_Service_Name)) {
					thekey = eachkey;
					getWatsonConceptExpansionParams(thekey, obj);
				} else if (eachkey.contains(WatsonQandA_Service_Name)) {
					thekey = eachkey;
					getWatsonQandAParams(thekey, obj);
				}
			}
		}
	}

	private void getWatsonQandAParams(String qakey, BasicDBObject vcapobj) {

		BasicDBList qadblist = (BasicDBList) vcapobj.get(qakey);
		vcapobj = (BasicDBObject) qadblist.get("0");
		// parse all the credentials from the vcap env variable
		vcapobj = (BasicDBObject) vcapobj.get("credentials");

		setQAURL((String) vcapobj.get("url"));
		setQAUserName((String) vcapobj.get("username"));
		setQAPassword((String) vcapobj.get("password"));
		
	}

	private void getWatsonConceptExpansionParams(String cekey,
			BasicDBObject vcapobj) {

		BasicDBList cedblist = (BasicDBList) vcapobj.get(cekey);
		vcapobj = (BasicDBObject) cedblist.get("0");
		// parse all the credentials from the vcap env variable
		vcapobj = (BasicDBObject) vcapobj.get("credentials");
		setCEURL((String) vcapobj.get("url"));
		setCEUserName((String) vcapobj.get("username"));
		setCEPassword((String) vcapobj.get("password"));
	}

	@SuppressWarnings("unchecked")
	public void getDBConnectionParams(String mysqldbkey, BasicDBObject vcapobj) {

		BasicDBList mysqldblist = (BasicDBList) vcapobj.get(mysqldbkey);
		vcapobj = (BasicDBObject) mysqldblist.get("0");
		// parse all the credentials from the vcap env variable
		vcapobj = (BasicDBObject) vcapobj.get("credentials");
		setDBHostName((String) vcapobj.get("hostname"));
		setDBName((String) vcapobj.get("name"));
		setDBPort((String) vcapobj.get("port"));
		setDBUser((String) vcapobj.get("username"));
		setDBPassword((String) vcapobj.get("password"));

	}

	public void setDBHostName(String dbHostname) {
		this.dbHost = dbHostname;

	}

	public String getDBHostName() {
		return this.dbHost;
	}

	public void setDBPort(String port) {
		this.dbPort = port;

	}

	public String getDBPort() {
		return this.dbPort;
	}

	public void setDBUser(String dbUser) {
		this.dbUser = dbUser;

	}

	public String getDBUser() {
		return this.dbUser;
	}

	public void setDBPassword(String dbPassword) {
		this.dbPassword = dbPassword;

	}

	public String getDBPassword() {
		return this.dbPassword;
	}

	
	public void setDBName(String dbName) {
		this.dbName = dbName;

	}

	public String getDBName() {
		return this.dbName;
	}
	
	public void setCEUserName(String ceUsername) {
		this.ceUsername = ceUsername;

	}

	public String getCEUserName() {
		return this.ceUsername;
	}
	
	public void setCEPassword(String cePassword) {
		this.cePassword = cePassword;

	}

	public String getCEPassword() {
		return this.cePassword;
	}
	
	public void setCEURL(String ceBaseURL) {
		this.ceBaseURL = ceBaseURL;

	}

	public String getCEURL() {
		return this.ceBaseURL;
	}
	

	public void setQAUserName(String qaUsername) {
		this.qaUsername = qaUsername;

	}

	public String getQAUserName() {
		return this.qaUsername;
	}
	
	public void setQAPassword(String qaPassword) {
		this.qaPassword = qaPassword;

	}

	public String getQAPassword() {
		return this.qaPassword;
	}
	
	public void setQAURL(String qaBaseURL) {
		this.qaBaseURL = qaBaseURL;

	}

	public String getQAURL() {
		return this.qaBaseURL;
	}

}
