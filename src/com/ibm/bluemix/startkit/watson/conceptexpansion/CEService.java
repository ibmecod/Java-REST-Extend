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

package com.ibm.bluemix.startkit.watson.conceptexpansion;

/*
 * Class to execute the Concept Expansion service running on Bluemix 
 * 
 * method initJob() : initiated a job with the service and returns a jobID
 * method getJobStatus() : checks the status of the job on the service
 * method getResult() : retrieves the result of the job
 */

/*
 * Author : Rajib Chakravorty
 * 
 */

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





public class CEService {
	
	//the identifier of the main JSONarray in the output
	public static String _returnSeedIdentifier       = "return_seeds";

	//the input seeds are separated by a comma
	public static String _seedSeparator = ",";
	
	//human readable format of the job status
	public static String _statusAwaiting  = "Awaiting Result";
	public static String _statusInFlight  = "In Flight";
	public static String _statusFailed    = "Failed";
	public static String _statusRetreived = "Retrieved";
	public static String _statusDone      = "Done";
	
	//mapping the status response from the service with the human readable format
	private static HashMap< String, String > _statusMap = new HashMap< String, String >();
	static{
		_statusMap.put( "A", CEService._statusAwaiting  );
		_statusMap.put( "G", CEService._statusInFlight  );
		_statusMap.put( "F", CEService._statusFailed    );
		_statusMap.put( "R", CEService._statusRetreived );
		_statusMap.put( "D", CEService._statusDone      );
	}

	// Since the service encode the response we need to use a hash to decode it. 
	//See @formatResult
	private static final HashMap<String, String> decoderHash = new HashMap<String, String>();
    static {
		decoderHash.put("zZzPeriodzZz"," ");
		decoderHash.put("zZzCommazZz",",");
		decoderHash.put("zZzSlashzZz","/");
		decoderHash.put("zZzColonzZz",":");
		decoderHash.put("zZzHashzZz","#");
		decoderHash.put("zZzStarzZz","*");
		decoderHash.put("zZzDashzZz","-");
		decoderHash.put("zZzAmpersandzZz","&");
		decoderHash.put("zZzPercentzZz","%");
		decoderHash.put("zZzSemicolonzZz",";");
		decoderHash.put("zZzUnderbarzZz","_");
		decoderHash.put("zZzOpenParenzZz","(");
		decoderHash.put("zZzCloseParenzZz",")");
		decoderHash.put("zZzPluszZz","+");
		decoderHash.put("zZzApostrophezZz","'");
		decoderHash.put("zZzQuestionMarkzZz","?");
		decoderHash.put("zZzEqualszZz","=");
		decoderHash.put("zZzDollarzZz","$");
		decoderHash.put("zZzBackslashzZz","\\");
		decoderHash.put("zZzPipezZz","|");
		decoderHash.put("zZzAtzZz","@");
		decoderHash.put("zZzExclamationzZz","!");
		decoderHash.put("zZzQuotezZz","\"");
		decoderHash.put("zZzSquareOpenzZz","[");
		decoderHash.put("zZzSquareClosezZz","]");
		decoderHash.put("zZzUnknownzZz","");
		
    }
    
	//these are from bluemix service
	private String _baseURL  ;
	private String _username ;
	private String _password ;
	
	//authentical string
	private String _authString       ;
 	private String _authHeaderString ;
 	
 	//refer to documentation : http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/apis/#!/Concept_Expansion
 	//the documentation is "incorrect" for example, the upload API is in /upload and not
 	// /conceptexpansion/service/upload
	private String _uploadURL ;
	private String _resultURL ;
	private String _statusURL ;
	
	private Executor _executor;
	
	
	//initiate the service handler with the configuration parameters
	public CEService( String baseURL, String username, String password ){
		
		_baseURL  = baseURL;
		_username = username;
		_password = password;

		_authString       = _username + ":" + _password;
		_authHeaderString = "Basic " + Base64.encodeBase64String( ( _authString ).getBytes() ) ;
		
		_uploadURL = _baseURL + "/upload" ;
		_resultURL = _baseURL + "/result" ;
		_statusURL = _baseURL + "/status" ;
		
		
		
		_executor = Executor.newInstance(); //.auth( _username, _password );

		//TODO: There is an API to check if the service is available;may be a good time
		//to check it; low priority
		
		
	}
	
	
	//initialize the job
	//@param seeds comma separated lists of seeds
	//@param label suggested label/groups for the seeds
	//#param dataset the dataset to be used
	
	//TODO : check if the dataset is permitted "twitter" or "mtsamples"
	public String initJob( String seeds, String label, String dataset ) 
			                                              throws JSONException, URISyntaxException, IOException {
		
		String[] seedArray = seeds.trim().split( _seedSeparator );
		
		JSONArray seedsArray = new JSONArray();
		for( String s : seedArray ){
			seedsArray.put( s );
		}
		
		JSONObject content = new JSONObject();
		content.put( "label"  , label      );
		content.put( "seeds"  , seedsArray );
		content.put( "dataset", dataset    );
		
		URI uploadURI = new URI( _uploadURL ).normalize();
		
		
		String uploadResponse = _executor.execute( Request.Post( uploadURI ) 
				                                   .addHeader( "Authorization", _authHeaderString  )
				                                   .addHeader("Accept", "application/json")
				                                   .bodyString( content.toString(), ContentType.APPLICATION_JSON ) ) 
				                                   .returnContent().toString();
		
		
		JSONObject returnedObject = new JSONObject( uploadResponse );
		
		String jobID = returnedObject.getString( "jobid" );
		
		return jobID;
		
	}
	
	/**
	 * Returns the status for a given job identifier.
	 *
	 * @param jobid the job identifier
	 * @return the status  e.g: 'D'  
	 * @throws URISyntaxException the URI syntax exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JSONException 
	 */
	public String getJobStatus( String jobid ) throws URISyntaxException, IOException, JSONException {
		
		ArrayList< NameValuePair > qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair( "jobid" , jobid ) );

		Executor executor = Executor.newInstance();
		// request the job status at baseURL+ '/status'
		URI serviceURI = new URI( _statusURL + "?" + URLEncodedUtils.format(qparams, "utf-8") ).normalize();
	    String statusResponse = executor.execute( Request.Get(serviceURI)
											     .addHeader("Authorization", _authHeaderString ) )
											     .returnContent().asString();

		JSONObject returnedObject = new JSONObject( statusResponse );
		
		String jobStatusCode = returnedObject.getString( "state" );
	    return ( String ) CEService._statusMap.get( jobStatusCode );
	}
	
	
	/**
	 * Third and final call to Concept Expansion Service
	 * PUT the jobid to the result request. This call is a PUT because the 
	 * data will be delete as soon as it is retrieved.
	 *
	 * @param jobid The job identifier
	 * @return JSONObject with the result of a given job if exists otherwise return null
	 * @throws URISyntaxException the URI syntax exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JSONException 
	 */
	public ArrayList< CEResponseObject > getResult( String jobid ) throws URISyntaxException, ClientProtocolException, IOException, JSONException {
		
		JSONObject content = new JSONObject();
    	content.put("jobid", jobid);
    	
		Executor executor = Executor.newInstance();
		// request the job status at baseURL+ '/status'
		URI serviceURI = new URI( _resultURL ).normalize();
	    String response = executor.execute( Request.Put(serviceURI)
										    .addHeader("Authorization", _authHeaderString )
										    .bodyString( content.toString(),ContentType.APPLICATION_JSON) )
										    .returnContent().asString();
	    
	    return formatResponse( response );
	}
	
	
	private ArrayList< CEResponseObject > formatResponse( String responseString ) throws JSONException{
		
		ArrayList< CEResponseObject > responses = new ArrayList< CEResponseObject >();
		
		
		JSONObject serviceResponseObject = new JSONObject( responseString );
		
		JSONArray returnSeedArray = serviceResponseObject.getJSONArray( CEService._returnSeedIdentifier );
		
		int totalReturnedSeed = returnSeedArray.length();
		
		for( int seed = 0 ; seed < totalReturnedSeed ; seed++ ){
			
			JSONObject returnedSeed = returnSeedArray.getJSONObject( seed );
			
			String result     = returnedSeed.getString( CEResponseObject._resultStringIdentifier     );
			String prevalence = returnedSeed.getString( CEResponseObject._prevalenceStringIdentifier );
			
			//response string contains string such as "zzzCommaszzz" representing comma
			//we need to convert these set strings as human readable strings
			for ( String key : decoderHash.keySet() ) {
				result = result.replaceAll(key,decoderHash.get( key ) );
			}
			
			CEResponseObject response = new CEResponseObject( result, prevalence );
			
			responses.add( response );
		}
		
		return responses;
	}
	
}
