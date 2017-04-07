package com.megatech.iot;

import java.util.Random;
import org.json.simple.JSONObject;

public class TagData {

	private String _tagId;
	private long _timestamp;
	private float _value;
	private float _valueLoLimit;
	private float _valueHiLimit;
	
	public TagData(String tagId){
		set_tagId(tagId);		
	}
		
	public String get_tagId() {
		return _tagId;
	}
	public void set_tagId(String tagId) {
		this._tagId = tagId;
	}
	public long get_timestamp() {
		return _timestamp;
	}
	public void set_timestamp(long timestamp) {
		this._timestamp = timestamp;
	}
	public float get_value() {
		return _value;
	}
	public void set_value(float value) {
		this._value = value;
	}

	public float get_valueLoLimit() {
		return _valueLoLimit;
	}
	public void set_valueLoLimit(float valueLoLimit) {
		this._valueLoLimit = valueLoLimit;
	}

	public float get_valueHiLimit() {
		return _valueHiLimit;
	}
	
	public void set_valueHiLimit(float valueHiLimit) {
		if (_valueLoLimit < valueHiLimit) // check validity of high limit input value
			this._valueHiLimit = valueHiLimit;
		else
			this._valueHiLimit = _valueLoLimit;
	}
	
	public void simulateValue() {
		// TODO Auto-generated method stub
		Random random = new Random();
		_value = _valueLoLimit + random.nextFloat() * (_valueHiLimit - _valueLoLimit); // generate random value in range 
		 
	}		
	
	public String generateJSON(){
		
		JSONObject obj=new JSONObject(); 
		obj.put("tag",_tagId); 
		obj.put("value",_value); 
		obj.put("timestamp",_timestamp); 
		System.out.println("JSON OBJ "+obj);
		return obj.toJSONString();
		
	}
	
}
