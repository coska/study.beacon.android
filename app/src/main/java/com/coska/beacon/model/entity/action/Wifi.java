package com.coska.beacon.model.entity.action;

import android.content.Context;
import android.net.wifi.WifiManager;

import org.json.JSONObject;

public class Wifi extends Action {

	public static final String WIFI_STATUS = "ON_OFF";

	protected Wifi(JSONObject json) {
		super(json);
	}

	private String getWifiStatus() {
		return json.optString(WIFI_STATUS);
	}

	@Override
	public void perform(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

		if (getWifiStatus().equals("ON"))
		{
			wifiManager.setWifiEnabled(true);
		}
		else
		{
			wifiManager.setWifiEnabled(false);
		}
	}
}
