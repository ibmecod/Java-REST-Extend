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


public class Answer
{


	public static String _answerTextIdentifier         = "text";
	public static String _answerConfidenceIdentifier   = "value";
	
	
	private String _answerText;
	private double _answerConfidence;
	
	public Answer( String answerText, double confidence ){
		
		_answerText       = answerText;
		_answerConfidence = confidence;
	}

	/**
	 * @return the _answerText
	 */
	public String getAnswerText() {
		return _answerText;
	}

	/**
	 * @return the _answerConfidence
	 */
	public double getAnswerConfidence() {
		return _answerConfidence;
	}
	
	
	public void print(){
		
		System.out.println( " Text : "       + _answerText );
		System.out.println( " Confidence : " + _answerConfidence );
		
	}
	
}

