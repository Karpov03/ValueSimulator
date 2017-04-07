package com.megatech.iot;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Asset {

	List<TagData> _tags1minute, _tags15minutes, _tags30minutes;
	String _topic, _broker, _clientId, _username, _password, _tagFile;
	int _qos;
	MqttHelper _mqttHelper;
	
	
	String fileName = "10001_tags.json";
	ClassLoader classLoader = ClassLoader.getSystemClassLoader();
	File tagfile = new File(classLoader.getResource(fileName).getFile());
	
	public Asset(String topic, String broker, String clientId, String username, String password, String tagFile, int qos){
		_topic = topic;
		_broker = broker;
		_clientId = clientId;
		_username = username;
		_password = password;
		_tagFile = tagFile;
		_qos = qos;		
	}
	
	public void initializeTags() {
		_tags1minute = new ArrayList();
		_tags15minutes = new ArrayList();
		_tags30minutes = new ArrayList();

		readTagsFromFileIntoArrays(_tagFile);

		System.out.println(_clientId + ": Tags read and initialized.");
	}
	
	public void runSimulation(){
		_mqttHelper = new MqttHelper(_topic, _broker, _clientId, _username, _password, _qos);
	System.out.println(_clientId);
		Simulator simulator1minute = new Simulator(_tags1minute, 1, _mqttHelper);
		simulator1minute.start();
		Simulator simulator15minutes = new Simulator(_tags15minutes, 15, _mqttHelper);
		simulator15minutes.start();
		Simulator simulator30minutes = new Simulator(_tags30minutes, 30, _mqttHelper);
		simulator30minutes.start();
	}

	private void readTagsFromFileIntoArrays(String tagFile) {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(tagfile));

			JSONObject jsonObject = (JSONObject) obj;
			// read json for each set of tags and populate in the corresponding
			// tags array
			processTagJSONIntoArray("tags_1_minute", _tags1minute, jsonObject);
			processTagJSONIntoArray("tags_15_minutes", _tags15minutes, jsonObject);
			processTagJSONIntoArray("tags_30_minutes", _tags30minutes, jsonObject);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processTagJSONIntoArray(String tagString, List<TagData> tagList, JSONObject jsonObject) {
		try {
			JSONArray tagsjson = (JSONArray) jsonObject.get(tagString);
			Iterator<JSONObject> iterator = tagsjson.iterator();

			while (iterator.hasNext()) {
				JSONObject nextJson = iterator.next();
				TagData newTag = new TagData(nextJson.get("tag").toString());
				newTag.set_valueLoLimit(Float.parseFloat(nextJson.get("valueLoLimit").toString()));
				newTag.set_valueHiLimit(Float.parseFloat(nextJson.get("valueHiLimit").toString()));
				tagList.add(newTag);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
