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
import java.util.List;


public class QuestionAnswer {
	String question;
	ArrayList<Answer> answerList;
	
	public QuestionAnswer()
	{
		question="";
		answerList = new ArrayList< Answer > ();
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public List<Answer> getAnswerList() {
		return answerList;
	}
	public void setAnswerList( ArrayList<Answer> answerList) {
		this.answerList = answerList;
	}	
	
	public void addAnswer( Answer ans ){
		answerList.add( ans );
	}
}