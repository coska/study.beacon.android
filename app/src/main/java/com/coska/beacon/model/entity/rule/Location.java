package com.coska.beacon.model.entity.rule;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

public class Location extends Rule {

	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String ACCURACY = "accuracy";

	protected Location(JSONObject json) {
		super(json);
	}

	private double getLatitude() {
		return json.optDouble(LATITUDE);
	}

	private double getLongitude() {
		return json.optDouble(LONGITUDE);
	}

	private double getAccuracy() {
		return json.optDouble(ACCURACY);
	}

	@Override
	public boolean isMatch(Context context) {
/*
		GoogleApiClient client = new GoogleApiClient.Builder(context)
				.addApi(LocationServices.API)
				.build();
		client.blockingConnect();

		try {
			android.location.Location location = LocationServices.FusedLocationApi.getLastLocation(client);
			// TODO: compare current location to scheduled one

			return false;

		} finally {
			client.disconnect();
		}
*/
		return true;
	}
}