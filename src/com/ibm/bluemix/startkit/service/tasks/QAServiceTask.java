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

import com.ibm.bluemix.startkit.response.Answer;
import com.ibm.bluemix.startkit.response.QuestionAnswer;
import com.ibm.bluemix.startkit.watson.qanda.QandAService;

public class QAServiceTask {
	
	public static int    _answersRequired = 5;
	public static String _dataset         = "healthcare";
	
	private QandAService _qaService;
	
	public QAServiceTask( String baseURL, String user, String password ){
		
		_qaService = new QandAService( baseURL, user, password );
	}
	
	public QuestionAnswer findAnswer( String question ) throws IOException, JSONException, URISyntaxException{
		
		System.out.println( question );
		ArrayList< Answer > answerSet = _qaService.getAnswer( question, _dataset, _answersRequired );
		
		QuestionAnswer qa = new QuestionAnswer();
		qa.setQuestion( question );
		qa.setAnswerList( answerSet );
		
		return qa;
		
	}

}
