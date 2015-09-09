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

package com.ibm.bluemix.startkit.response;

import java.util.ArrayList;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class QAResponse {
	
	
	public static String _questionIdentifier       = "question";
	public static String _answerTextIdentifier     = "text";
	public static String _answerConfIdentifier     = "confidence";
	public static String _answerListIdentifier     = "answers";
	
	public static String _quesansListIdenntifier   = "qa";
	
	
	
	ArrayList<QuestionAnswer> questionAnswerList;
	
	public QAResponse() 
	{
		questionAnswerList = new ArrayList< QuestionAnswer >();
	}
	
	public ArrayList< QuestionAnswer > getQAList(){
		return questionAnswerList;
	}
	
	
	public void addQA( QuestionAnswer qa ){
		questionAnswerList.add( qa );
	}
	
	public String prepareJSONString() throws JSONException{
		
		JSONObject responseJSON = new JSONObject();
		
		//qa
		JSONArray qaArray = new JSONArray();
		
		for( QuestionAnswer qa : questionAnswerList ){
			
			JSONObject qaObject = new JSONObject();
			
			qaObject.put( _questionIdentifier, qa.getQuestion() );
			
			JSONArray answers = new JSONArray();
			for( Answer ans : qa.answerList ){
				
				JSONObject answerObj = new JSONObject();
				answerObj.put( _answerTextIdentifier, ans.getAnswerText() );
				answerObj.put( _answerConfIdentifier, ans.getAnswerConfidence() );
				
				answers.put( answerObj );
				
			}
			qaObject.put( _answerListIdentifier, answers );
			
			qaArray.put( qaObject );
			
		}
		responseJSON.put( _quesansListIdenntifier, qaArray );
		return responseJSON.toString();
	}
	
}