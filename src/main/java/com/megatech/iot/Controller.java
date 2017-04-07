package com.megatech.iot;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Controller {

	static List<Asset> _assets;
	
	
	static String fileName = "assets.json";
	static ClassLoader classLoader = ClassLoader.getSystemClassLoader();
	static File assetfile = new File(classLoader.getResource(fileName).getFile());
		
	private static void readAssetsFromFileIntoArray(String assetFile) {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(assetfile));

			JSONObject jsonObject = (JSONObject) obj;
			// read json for each set of tags and populate in the corresponding
			// tags array
			processAssetJSONIntoArray("assets", _assets, jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	private static void processAssetJSONIntoArray(String assetString, List<Asset> assetList, JSONObject jsonObject) {
		try {
			JSONArray assetsJson = (JSONArray) jsonObject.get(assetString);
			Iterator<JSONObject> iterator = assetsJson.iterator();

			while (iterator.hasNext()) {
				JSONObject nextJson = iterator.next();
				
				String clientId = nextJson.get("clientId").toString();
				String topic = nextJson.get("topic").toString();
				String broker = nextJson.get("broker").toString();
				String username = nextJson.get("username").toString();
				String password = nextJson.get("password").toString();
				int qos = (Integer.parseInt(nextJson.get("qos").toString()));
				String tagFile = nextJson.get("tagFile").toString();

				Asset newAsset = new Asset(topic, broker, clientId, username, password, tagFile, qos);
				assetList.add(newAsset);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void simulatedata() {
		// TODO Auto-generated method stub

		_assets = new ArrayList<Asset>();
		readAssetsFromFileIntoArray("assets.json");
		
		Iterator<Asset> assetIterator = _assets.iterator();
		while (assetIterator.hasNext()) {
			Asset a = assetIterator.next();
			a.initializeTags();
			a.runSimulation();
		}
		
	}
}



	