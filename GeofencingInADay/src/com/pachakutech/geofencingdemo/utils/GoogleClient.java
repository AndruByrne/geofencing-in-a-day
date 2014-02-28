package com.pachakutech.geofencingdemo.utils;

import java.io.IOException;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.pachakutech.geofencingdemo.structs.*;

public class GoogleClient {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static HttpRequestFactory REQUEST_FACTORY;

    public GoogleClient() {
        /* creating the transport for the data to server */
        REQUEST_FACTORY = HTTP_TRANSPORT
			.createRequestFactory(new HttpRequestInitializer() {
				@Override
				public void initialize(HttpRequest request) {
					/* use JacksonFactory to add a JSON parser to the request*/
					request.setParser(new JsonObjectParser(JSON_FACTORY));
				}
			});

    }
	public Places executePlacesSearchGet(String keyword, String key, String location, int radius) {
        /* Define all parameters that are part of the URL */
        PlacesSearchUrl url = new PlacesSearchUrl();
        url.key = key;
        url.location = location;
        url.radius = radius;
        url.sensor = true;
        url.keyword = keyword;

        try {
            /* Build the request and execute it */
            HttpRequest request = REQUEST_FACTORY.buildGetRequest(url);
            HttpResponse response = request.execute();
            // System.out.println(response.parseAsString());

            Places places = response.parseAs(Places.class);
            return places;

        } catch (IOException e) {
            e.printStackTrace();
			return null;
        }

    }
}
