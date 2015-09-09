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

package com.ibm.bluemix.startkit.service.tasks;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.json.JSONException;

import com.ibm.bluemix.startkit.watson.conceptexpansion.CEResponseObject;
import com.ibm.bluemix.startkit.watson.conceptexpansion.CEService;


public class CEServiceTask {
	
	public static String _dataset = "mtsamples";
	public static String _label   = "disease";

	public static String _conceptDelim = ",";
	
	
	
	
	//A simple blocking method is used to check the job status
	//Once after a check, the thread sleeps for _stepSleepTime milliseconds
	//After a trial of _maxWaitSteps, the trial is abandoned and the
	//execution finished failing
	
	private static int _stepSleepTime = 1000 ; // in millisecond
	private static int _maxWaitSteps  = 500;   // in number of steps; waiting time in ms = _stepSleeptime x _maxWaitSteps

	
	private CEService _ceService;
	
	private int                _keywordLengthThreshold = 4;
	
	public CEServiceTask( String baseURL, String user, String password ){
		
		_ceService = new CEService( baseURL, user, password );
	}
	
	public ArrayList< String > getConcepts( String keyword, int topConceptToPick ) 
			throws JSONException, URISyntaxException, IOException, InterruptedException{
		
	
		ArrayList< String > expandedConcepts = new ArrayList< String >();
		
		String[] inputConcepts = keyword.split( _conceptDelim );
		
		for( String inputConcept : inputConcepts ){
			
			if( !expandedConcepts.contains( ( String ) ( inputConcept.toLowerCase() ) ) ){
				
				expandedConcepts.add( inputConcept.toLowerCase() );
			}
		}
		
		//initiate the job with the params and get a job id
		String jobID = _ceService.initJob( keyword, _label, _dataset );
		
		//continues to check the status of the job
		//until a maximum time out is reached or the job status is
		//anything but "Done"
		boolean jobStatus = checkJobStatus( jobID, _ceService );
		
		if( !jobStatus ){
			
			return null;
		}
		
		//if job is Done, retrieve the result
		ArrayList< CEResponseObject > responses = _ceService.getResult( jobID );
		
		//print what was retrieved
		//for( CEResponseObject response : responses ){
			
		//	response.print();
		//}
		
		
		int responsedToBrowse = Math.min( responses.size(), topConceptToPick );
		for( int r = 0 ; r < responsedToBrowse; r++ ){
			
			String result = responses.get( r ).getResult();
			
			String[] keyResults = result.split( _conceptDelim );
			
			for( String keyResult : keyResults ){
				
				if( keyResult.length() <  _keywordLengthThreshold ){
					continue;
				}
				
				if( !expandedConcepts.contains( (String) ( keyResult.toLowerCase() ).trim() ) ){
					
					expandedConcepts.add( ( keyResult.toLowerCase() ).trim() );
				}
			}
		}
		
		return expandedConcepts;
		
	}
	
	/*
	 * Static method to check the job status
	 * 
	 * 
	 */
	
	public static boolean checkJobStatus( String jobID, CEService ce ) throws URISyntaxException, IOException, JSONException, InterruptedException{
		
		/* simple logic : wait for a maximum time out period for the job
		 * status to go "Done"
		 */
		
		String jobStatus = CEService._statusAwaiting;
		
		int countStep = 0 ;
		
		
		while( !jobStatus.equals( CEService._statusDone ) ){
			
			jobStatus = ce.getJobStatus( jobID );
			
			System.out.println( "Job Status : " + jobStatus );
			
			countStep++;
				
			if( countStep >= _maxWaitSteps ){
				
				//waited for some max time and still has not got a "Done" response
				//quit waiting
				
				System.out.println( "Maximum waiting time reached. Quitting. " );
				return false;
			}
			
			//Try again some time later
			Thread.sleep( _stepSleepTime );
			
			
		}
		
		return true;
		
	}
	
	

}
