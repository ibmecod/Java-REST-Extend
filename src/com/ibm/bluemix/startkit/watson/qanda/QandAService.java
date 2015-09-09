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

package com.ibm.bluemix.startkit.watson.qanda;


/*
 * Class to execute and use the QandA service on Bluemix and retrieve
 * a set of answers given a question and dataset to work with 
 * 
 * 
 */

/*
 * 
 * Author : Rajib Chakravorty
 */

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.bluemix.startkit.response.Answer;

public class QandAService {

	
	/*
	 * The output json from the service looks like
	 * {
		   "question": {
		   		"formattedAnswer" : boolean,
		   		"status" :
		   		"..":
		   		"answers": [
		   			
		   			{
		   				"id" : ...,
		   				"text": ...,
		   				"pipeline":...,
		   				"confidence":...
		   			}
		   			
		   		],
		   		"..."
		   		
		   }
		}
	 * 
	 * For our purpose, the important params are "text" and "confidence"
	 * This is modeled as AnswerObject
	 */
	public static String _mainObjectIdentifier   = "question";
	public static String _answerObjectIdentifier = "answers" ;
	
	/*
	 * The input json looks like
	 * {
		   "question": {
		      "questionText":"Question text here"
		   }
		}
	 * 
	 */
	public static String _questionTextIdentifier = "questionText";
	public static String _itemsIdentifier        = "items";
	public static String _questionIdentifier     = "question";
	public static String _evidenceIdenitifer     = "evidenceRequest";
	
	public static String _evidenceListIdentifier = "evidencelist";

	
	
	//refer to API document: 
	//http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/apis/#!/Question_Answer/question
	private String _qandaServiceURL;
	
	private Executor _executor;
	
	
	//initiate the service handler with the configuration parameters
	public QandAService( String baseURL, String username, String password ){
		
		_executor = Executor.newInstance().auth( username, password );

		_qandaServiceURL = baseURL + "/v1/question/";
		
		//TODO: There is an API to check if the service is available;may be a good time
		//to check it; low priority
		
		
	}
	
	public String getAnswerString( String question, String dataset, int items ) 
											throws IOException, JSONException, URISyntaxException {
	
		//input json preparation
		JSONObject questionJson = new JSONObject();
		questionJson.put( QandAService._questionTextIdentifier, question );
		
		JSONObject evidenceRequest = new JSONObject();
		evidenceRequest.put( QandAService._itemsIdentifier, items);
		
		questionJson.put( QandAService._evidenceIdenitifer, evidenceRequest);
		
		JSONObject postData = new JSONObject();
    	postData.put( QandAService._questionIdentifier, questionJson);
    	
   	
		URI serviceURI = new URI( _qandaServiceURL + dataset ).normalize();
		
		
		Request postRequest = Request.Post( serviceURI );
		postRequest.addHeader("Accept", "application/json");
		postRequest.addHeader("X-SyncTimeout", "30");
		postRequest.bodyString( postData.toString(), ContentType.APPLICATION_JSON );
		
		
	
		Content content = _executor.execute( postRequest).returnContent();
		String serviceAnswer = content.asString();
		
		System.out.println( serviceAnswer );
		return serviceAnswer;
	}
	//main method to call the service, retrieve the result and format it in a list of Java object
	//TODO : check if the dataset is permitted "travel" or "healthcare"
	public ArrayList< Answer > getAnswer( String question, String dataset, int items ) 
											throws IOException, JSONException, URISyntaxException {
		
		String serviceAnswer = getAnswerString( question, dataset, items );
		
		System.out.println( serviceAnswer );
		return formatAnswers( serviceAnswer );
    		
    		
	}
	

	
	//method to convert the returned string of the service
	//into a list of answer objects
	private ArrayList< Answer > formatAnswers( String answer ) throws JSONException{
		System.out.println( answer );
		ArrayList< Answer > answers = new ArrayList< Answer >();
		
		JSONObject serviceResponseObject = ( ( JSONObject ) ( ( new JSONArray( answer ) ).get( 0 ) ) ).getJSONObject( _mainObjectIdentifier );
		
		JSONArray answerList = ( JSONArray ) serviceResponseObject.getJSONArray( _evidenceListIdentifier );
		
		int totalAnswerReceived = answerList.length();
		
		for( int i = 0 ; i < totalAnswerReceived; i++ ){
			
			JSONObject ansObject = ( JSONObject ) answerList.get( i );
			
			if( ansObject.has( Answer._answerTextIdentifier ) ){
				Answer answerObject = new Answer( ansObject.getString( Answer._answerTextIdentifier       ),
					                                      ansObject.getDouble( Answer._answerConfidenceIdentifier ) );
				answers.add( answerObject );
				
			}
			
		}
		
		return answers;
	}
		
}
