/**
 *****************************************************************************
 * Copyright (c) 2015 IBM Corporation and other Contributors.

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Sathiskumar Palaniappan - Initial Contribution
 *****************************************************************************
 */

package com.ibm.iotf.sample.client.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.iotf.client.IoTFCReSTException;
import com.ibm.iotf.client.api.APIClient;
import com.ibm.iotf.client.app.ApplicationClient;
import com.ibm.iotf.sample.util.Utility;

/**
 * This sample showcases various ReST operations that can be performed on IoT Foundation to
 * add/update/get/delete device type(s)
 */
public class SampleDeviceTypeAPIOperations {
	
	private final static String PROPERTIES_FILE_NAME = "application.prop";
	private final static String DEFAULT_PATH = "samples/iotfdeviceclient/src";
	
	/**
	 * Example Json format to add a device type
	 * 
	 * {
		  "id": "SampleDT1",
		  "description": "SampleDT1",
		  "deviceInfo": {
		        "fwVersion": "1.0.0",
		    "hwVersion": "1.0"
		  },
		  "metadata": {
		
		  }
	 * }
	 */
	private final static String deviceTypeToBeAdded = "{\"id\": \"SampleDT1\",\"description\": "
			+ "\"SampleDT1\",\"deviceInfo\": {\"fwVersion\": \"1.0.0\",\"hwVersion\": \"1.0\"},\"metadata\": {}}";
	
	private APIClient apiClient = null;
	
	SampleDeviceTypeAPIOperations(String filePath) {
		
		/**
		 * Load properties file "application.prop"
		 */
		Properties props = Utility.loadPropertiesFile(PROPERTIES_FILE_NAME, filePath);
		try {
			//Instantiate the class by passing the properties file
			this.apiClient = new APIClient(props);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public static void main(String[] args) throws IoTFCReSTException {
	
		String fileName = null;
		if (args.length == 1) {
			fileName = args[0];
		} else {
			fileName = Utility.getDefaultFilePath(PROPERTIES_FILE_NAME, DEFAULT_PATH);
		}
		
		SampleDeviceTypeAPIOperations sample = new SampleDeviceTypeAPIOperations(fileName);
		
		sample.getAllDeviceTypes();
		sample.addDeviceType();
		sample.deleteDeviceType();
		sample.addDeviceType();
		sample.getDeviceType();
		sample.updateDeviceType();
	}

	/**
	 * This sample showcases how to Delete a device type using the Java Client Library.
	 * @throws IoTFCReSTException
	 */
	private void deleteDeviceType() throws IoTFCReSTException {
		try {
			boolean status = this.apiClient.deleteDeviceType("SampleDT1");
			System.out.println(status);
		} catch(IoTFCReSTException e) {
			System.out.println("HttpCode :" + e.getHttpCode() +" ErrorMessage :: "+ e.getMessage());
			// Print if there is a partial response
			System.out.println(e.getResponse());
		}
	}

	/**
	 * This sample showcases how to Create a device type using the Java Client Library. 
	 * @throws IoTFCReSTException
	 */
	private void addDeviceType() throws IoTFCReSTException {
		try {
			JsonElement input = new JsonParser().parse(deviceTypeToBeAdded);
			JsonObject response = this.apiClient.addDeviceType((JsonObject) input);
			System.out.println(response);
		} catch(IoTFCReSTException e) {
			System.out.println("HttpCode :" + e.getHttpCode() +" ErrorMessage :: "+ e.getMessage());
			// Print if there is a partial response
			System.out.println(e.getResponse());
		}
	}
	
	/**
	 * This sample showcases how to get the details of a device type using the Java Client Library.
	 * @throws IoTFCReSTException
	 */
	private void getDeviceType() throws IoTFCReSTException {
		try {
			JsonObject response = this.apiClient.getDeviceType("SampleDT1");
			System.out.println(response);
		}  catch(IoTFCReSTException e) {
			System.out.println("HttpCode :" + e.getHttpCode() +" ErrorMessage :: "+ e.getMessage());
			// Print if there is a partial response
			System.out.println(e.getResponse());
		}
	}

	/**
	 * This sample showcases how to update a device type using the Java Client Library.
	 * @throws IoTFCReSTException
	 */
	private void updateDeviceType() throws IoTFCReSTException {
		try {
			JsonObject json = new JsonObject();
			json.addProperty("description", "Hello, I'm updated description");
			JsonObject response = this.apiClient.updateDeviceType("SampleDT1", json);
			System.out.println(response);
		}  catch(IoTFCReSTException e) {
			System.out.println("HttpCode :" + e.getHttpCode() +" ErrorMessage :: "+ e.getMessage());
			// Print if there is a partial response
			System.out.println(e.getResponse());
		}
	}
	
	/**
	 * This sample showcases how to retrieve all the device types present in the given Organization.
	 * @throws IoTFCReSTException
	 */
	private void getAllDeviceTypes() throws IoTFCReSTException {
		// Get all the devices in the organization
		/**
		 * The Java ibmiotf client library provides an one argument constructor
		 * which can be used to control the output, for example, lets try to retrieve
		 * the devices in a sorted order based on device type ID.
		 */
		
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("_sort","id"));
		
		try {
			JsonObject response = this.apiClient.getDeviceTypes(parameters);
			
			
			// The response will contain more parameters that will be used to issue
			// the next request. The result element will contain the current list of devices
			JsonArray deviceTypes = response.get("results").getAsJsonArray(); 
			for(Iterator<JsonElement> iterator = deviceTypes.iterator(); iterator.hasNext(); ) {
				JsonElement deviceElement = iterator.next();
				JsonObject responseJson = deviceElement.getAsJsonObject();
				System.out.println(responseJson);
			}
		}  catch(IoTFCReSTException e) {
			System.out.println("HttpCode :" + e.getHttpCode() +" ErrorMessage :: "+ e.getMessage());
			// Print if there is a partial response
			System.out.println(e.getResponse());
		}
	}
}