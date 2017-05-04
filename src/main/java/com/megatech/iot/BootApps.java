package com.megatech.iot;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class BootApps extends SpringBootServletInitializer {

	static List<Asset> _assets;

	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return configureApplication(builder);
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		configureApplication(new SpringApplicationBuilder()).run(args);
		Resource resource = new ClassPathResource("assets.json");
		FileReader fr = new FileReader(resource.getFile());

		_assets = new ArrayList<Asset>();
		readAssetsFromFileIntoArray(fr);

		Iterator<Asset> assetIterator = _assets.iterator();
		while (assetIterator.hasNext()) {
			Asset a = assetIterator.next();
			a.initializeTags();
			a.runSimulation();
		}
	}

	private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
		return builder.sources(BootApps.class).bannerMode(Banner.Mode.OFF);
	}

	private static void readAssetsFromFileIntoArray(FileReader fr) {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(fr);

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

}
