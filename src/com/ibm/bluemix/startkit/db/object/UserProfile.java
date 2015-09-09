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

package com.ibm.bluemix.startkit.db.object;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfile {
	
	public static String _userIDKey     = "user_id"    ;
	public static String _nameKey       = "name"       ;
	public static String _ageKey        = "age"        ;
	public static String _ethnicityKey  = "ethnicity"  ;
	public static String _professionKey = "profession" ;
	public static String _interestsKey  = "interests"  ;
	public static String _zipKey        = "zip"        ;
	public static String _fitnessKey    = "fitness"    ;
	public static String _passwordKey   = "password"   ;
	
	private String _userID;
	
	private String _name;
	
	private int    _age;
	
	private String _ethnicity;
	
	private String _profession;
	
	private String _interests;
	
	private String _zip;
	
	private String _fitnessRoutine;
	
	private String _password;
	
	public UserProfile( String userID,
			            String name,
					    int    age,
					    String zip,
					    String ethnicity,
					    String fitnessRoutine,
					    String profession,
					    String interests,
					    String password ){
		
		setID     ( userID      );
		setName          ( name           );
		setAge           ( age            );
		setEthnicity     ( ethnicity      );
		setProfession    ( profession     );
		setInterests     ( interests      );
		setZip           ( zip            );
		setFitnessRoutine( fitnessRoutine );
		setPassword      ( password       );
	}
	
	
	public String getName(){
		return _name;
	}
	
	public void setName( String name ){
		_name = name;
	}

	public String getID() {
		return _userID;
	}

	public void setID(String id) {
		this._userID = id;
	}

	public int getAge() {
		return _age;
	}

	public void setAge(int age) {
		this._age = age;
	}

	public String getEthnicity() {
		return _ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this._ethnicity = ethnicity;
	}

	public String getProfession() {
		return _profession;
	}

	public void setProfession(String profession) {
		this._profession = profession;
	}

	public String getInterests() {
		return _interests;
	}

	public void setInterests(String interests) {
		this._interests = interests;
	}

	public String getZip() {
		return _zip;
	}

	public void setZip(String zip) {
		this._zip = zip;
	}

	public String getFitnessRoutine() {
		return _fitnessRoutine;
	}

	public void setFitnessRoutine(String fitnessRoutine) {
		this._fitnessRoutine = fitnessRoutine;
	}

	public String getPassword() {
		return _password;
	}

	public void setPassword(String password) {
		this._password = password;
	}
	
	public JSONObject createProfileJSON() throws JSONException{
		
		JSONObject profileObject = new JSONObject();
		
		profileObject.put( UserProfile._ageKey       , _age            );
		profileObject.put( UserProfile._ethnicityKey , _ethnicity      );
		profileObject.put( UserProfile._fitnessKey   , _fitnessRoutine );
		profileObject.put( UserProfile._interestsKey , _interests      );
		profileObject.put( UserProfile._nameKey      , _name           );
		profileObject.put( UserProfile._passwordKey  , _password       );
		profileObject.put( UserProfile._professionKey, _profession     );
		profileObject.put( UserProfile._userIDKey    , _userID         );
		profileObject.put( UserProfile._passwordKey  , _password       );
		profileObject.put( UserProfile._zipKey       , _zip            );

		
		
		return profileObject;
	}

}
