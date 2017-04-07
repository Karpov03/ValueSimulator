package com.megatech.iot;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttHelper {
	String _topic;
	int _qos;
	String _broker;
	String _clientId;
	String _username;
	String _password;
	MqttClient _mqttClient;
	int keepAliveInterval = 60 * 60; // mqtt keep alive for 1 hour

	public MqttHelper(String topic, String broker, String clientId, String username, String password, int qos) {

		setTopic(topic);
		setBroker(broker);
		setClientId(clientId);
		setUsername(username);
		setPassword(password);
		setQos(qos);

		makeConnection();
	}

	private void setTopic(String topic) {
		this._topic = topic;
	}

	private void setClientId(String clientId) {
		this._clientId = clientId;
	}

	private void setUsername(String username) {
		this._username = username;
	}

	private void setPassword(String password) {
		this._password = password;
	}

	private void setBroker(String broker) {
		this._broker = broker;
	}

	private void setQos(int qos) {
		this._qos = qos;
	}

	public void sendMessage(String content) {
		try {

			MqttMessage message = new MqttMessage(content.getBytes());
			System.out.println("Publishing message: " + content);
			System.out.println("Publishing message: " + message.toString());
			message.setQos(_qos);
			if (_mqttClient != null) {

				if (!_mqttClient.isConnected())
					makeConnection();

				_mqttClient.publish(_topic, message);
				System.out.println("Message published");
			} else
				System.out.println("Mqtt client invalid!!!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void makeConnection() {

		MemoryPersistence persistence = new MemoryPersistence();

		try {
			_mqttClient = new MqttClient(_broker, _clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();

			connOpts.setUserName(_username);
			connOpts.setPassword(_password.toCharArray());
			connOpts.setKeepAliveInterval(keepAliveInterval);
			connOpts.setCleanSession(true);
			System.out.println("Connecting to broker: " + _broker);
			_mqttClient.connect(connOpts);
			System.out.println("Connected.");

		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			_mqttClient.disconnect();
			System.out.println("Disconnected");
			System.exit(0);
		} catch (Exception e) {
		}
	}

}
