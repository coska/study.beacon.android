package com.coska.beacon.model.entity.action;

import android.content.Context;
import android.net.wifi.WifiManager;

import org.json.JSONObject;

public class Wifi extends Action {

	public static final String WIFI_STATUS = "status";

	protected Wifi(JSONObject json) {
		super(json);
	}

	private boolean getStatus() {
		return json.optBoolean(WIFI_STATUS);
	}

	@Override
	public void perform(Context context) {
		((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(getStatus());
	}
}