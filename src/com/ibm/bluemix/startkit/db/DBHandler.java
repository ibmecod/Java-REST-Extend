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

package com.ibm.bluemix.startkit.db;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class DBHandler {
	
	private Connection _connection;
	
	public DBHandler( String host, String port, String user, String password, String dbName ) 
			                               			throws ClassNotFoundException, SQLException{

		Class.forName("com.mysql.jdbc.Driver");
		
		_connection = DriverManager.getConnection( "jdbc:mysql://" + host + ":" + port + "/" + dbName , user, password );
		
	}
	
    public ResultSet runSelectQuery( String queryString) throws SQLException {
            
            Statement stmt = _connection.createStatement();
            
            return stmt.executeQuery( queryString );

    }
    
    public boolean runInsertQuery( String queryString){
        
    	try{ 
    		Statement stmt = _connection.createStatement();
        
    		stmt.executeUpdate( queryString );
    		return true;
    		
    	}
    	
    	catch( SQLException sqle ){
    		
    		return false;
    	}

    }
    
    public void closeConnection() throws SQLException{
    	
    	_connection.close();
    }

}
