package com.agilerules.iotf.sample.client.application;

import java.io.IOException;
import java.util.Properties;

import com.agilerules.iotf.sample.client.SystemObject;
import com.google.gson.JsonObject;
import com.ibm.iotf.client.app.ApplicationClient;

/**
 * 
 * This sample shows how an application publish a device event 
 * using MQTT to IBM Watson IoT Platform on behalf of the device.
 *
 */
public class MQTTApplicationDeviceEventPublish {

	private final static String PROPERTIES_FILE_NAME = "/application.properties";

	public static void main(String[] args) throws Exception {
		/**
		  * Load device properties
		  */
		Properties props = new Properties();
		try {
			props.load(MQTTApplicationDeviceEventPublish.class.getResourceAsStream(PROPERTIES_FILE_NAME));
		} catch (IOException e1) {
			System.err.println("Not able to read the properties file, exiting..");
			System.exit(-1);
		}
		
		ApplicationClient myClient = null;
		try {
			//Instantiate the class by passing the properties file
			myClient = new ApplicationClient(props);
			myClient.connect();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		/**
		 * Get the Device Type and Device Id on behalf the application will publish the event
		 */
		String deviceType = trimedValue(props.getProperty("Device-Type"));
		String deviceId = trimedValue(props.getProperty("Device-ID"));

		SystemObject obj = new SystemObject();
		/**
		 * Publishes this process load event for every 5 second
		 */
		while(true) {
			boolean code = false;
			try {
				
				//Generate a JSON object of the event to be published
				JsonObject event = new JsonObject();
				event.addProperty("name", SystemObject.getName());
				event.addProperty("cpu",  obj.getProcessCpuLoad());
				event.addProperty("mem",  obj.getMemoryUsed());
				
				// publish the event on behalf of device
				code = myClient.publishEvent(deviceType, deviceId, "blink", event);
			
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(code == true) {
				System.out.println("Published the event successfully !");
			} else {
				System.out.println("Failed to publish the event......");
				System.exit(-1);
			}
		}
	}
	
	private static String trimedValue(String value) {
		if(value != null) {
			return value.trim();
		}
		return value;
	}
}
