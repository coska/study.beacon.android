package com.coska.beacon.model.entity.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.json.JSONObject;

public class PhoneCall extends Action {

	public static final String NAME = "name";
	public static final String DIAL_NUMBER = "dial_number";

	protected PhoneCall(JSONObject json) {
		super(json);
	}

	private String getDialNumber() {
		return json.optString(DIAL_NUMBER);
	}

	@Override
	public void perform(Context context) {
		Uri uri = Uri.fromParts("tel", getDialNumber(), null);
		context.startActivity(new Intent(Intent.ACTION_DIAL, uri));
	}
}