package com.coska.beacon.model.entity.action;

import android.content.Context;

import org.json.JSONObject;

public class Wifi extends Action {

	protected Wifi(JSONObject json) {
		super(json);
	}

	@Override
	public void perform(Context context) {
		// TODO: turn on or off wifi connection
		
	}
}