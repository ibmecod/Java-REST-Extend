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
 * Class to hold concept expansion service results
 */

/*
 * 
 * Author : Rajib Chakravorty
 */

public class CEResponseObject {
	
	public static String _resultStringIdentifier     = "result";
	public static String _prevalenceStringIdentifier = "prevalence";
	
	private String _result;
	
	private double    _prevalence;
	
	public CEResponseObject( String result, String prevalence ){
		
		_result     = result;
		_prevalence = Double.parseDouble( prevalence );
	}

	/**
	 * @return the _result
	 */
	public String getResult() {
		return _result;
	}

	/**
	 * @return the _prevalence
	 */
	public double getPrevalence() {
		return _prevalence;
	}
	
	public void print(){
		
		System.out.println( " Result     :" + _result );
		System.out.println( " Prevalance :" + _prevalence );
		
	}

}
