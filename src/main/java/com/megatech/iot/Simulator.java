package com.megatech.iot;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class Simulator extends Thread {

	private List<TagData> _tagList;
	private int _delay;
	private MqttHelper _mqttHelper;

	public Simulator(List<TagData> tagList, int delay, MqttHelper mqttHelper) {

		_tagList = tagList;
		_delay = delay;
		_mqttHelper = mqttHelper;

	}

	public void run() {
		runSimulation();

	}

	private void generateRecords(List<TagData> tagList) {

		long timestamp = System.currentTimeMillis();

		for (TagData t : tagList) {
			t.simulateValue();
			t.set_timestamp(timestamp);
		}
	}

	private void runSimulation() {
		try {
			while (true) {
				generateRecords(_tagList);
				System.out.println(_tagList.get(0).get_tagId() + ":" + _tagList.get(0).get_timestamp() + " : Tag values simulated.");
				Iterator<TagData> tagIterator = _tagList.iterator();
				while (tagIterator.hasNext()) {
					TagData t = tagIterator.next();
					String content = t.generateJSON();
					System.out.println("CONTENT :"+content);
					_mqttHelper.sendMessage(content);
				}
				TimeUnit.MINUTES.sleep(_delay);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
