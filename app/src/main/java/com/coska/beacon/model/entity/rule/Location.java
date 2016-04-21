package com.coska.beacon.model.entity.rule;

import android.content.Context;

import org.json.JSONObject;

public class Location extends Rule {

	protected Location(JSONObject json) {
		super(json);
	}

	@Override
	public boolean isMatch(Context context) {
		// TODO: implement location checking synchronously
		return false;
	}
}