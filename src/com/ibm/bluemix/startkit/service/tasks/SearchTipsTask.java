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
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONException;

import com.ibm.bluemix.startkit.response.QAResponse;
import com.ibm.bluemix.startkit.response.QuestionAnswer;

public class SearchTipsTask {
	
	private CEServiceTask      _ceServiceStep        = null;
	private QAServiceTask      _qaServiceStep		 = null;
	
	private int                _numConceptToPick     = 3; //Integer.MAX_VALUE;
	
	public SearchTipsTask(  ){
		
	}
	
	public void createCEService( String baseURL, String user, String password ){
		
		if( _ceServiceStep == null ){
			_ceServiceStep = new CEServiceTask( baseURL, user, password );
		}
	}
	
	public void createQAService( String baseURL, String user, String password ){
		
		if( _qaServiceStep == null ){
			_qaServiceStep = new QAServiceTask( baseURL, user, password );
		}
	}
	
	public void setTopConceptToUse( int threshold ){
		_numConceptToPick = threshold;
	}
	
	public String runSteps( String keywords ) 
			throws JSONException, URISyntaxException, IOException, InterruptedException, SQLException{
		
		
		//use the keyword for concept expansion
		//basically prepare a list of strings
		
		ArrayList< String > expandedKeyword = _ceServiceStep.getConcepts( keywords, _numConceptToPick );
		
		
		QAResponse mrp = new QAResponse();
		
		//now ask the questions
		
		
		String concatanatedString = "";
		
		for( int conceptNo = 0 ; conceptNo < expandedKeyword.size(); conceptNo++ ){
			
			concatanatedString = concatanatedString.concat( expandedKeyword.get( conceptNo ) );
			if( conceptNo != ( expandedKeyword.size() - 1 ) ){
				concatanatedString = concatanatedString.concat( "," );
			}
		}
		
		String question;
		
		
		question = "What is " + concatanatedString + "?";
		QuestionAnswer qaOne = _qaServiceStep.findAnswer( question );
		mrp.addQA( qaOne );
		
		//System.out.println( qaOne.getAnswerList().get( 1 ).getAnswerText()  );
		
		question = "What should I know about " + concatanatedString + "?";
		QuestionAnswer qaTwo = _qaServiceStep.findAnswer( question );
		mrp.addQA( qaTwo );
		
		
		return mrp.prepareJSONString();		
	}
}
