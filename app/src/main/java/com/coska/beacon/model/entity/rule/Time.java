package com.coska.beacon.model.entity.rule;

import android.content.Context;

import org.json.JSONObject;

public class Time extends Rule {

	protected Time(JSONObject json) {
		super(json);
	}

	@Override
	public boolean isMatch(Context context) {
		// TODO: implement timestamp checking
		return false;
	}
}