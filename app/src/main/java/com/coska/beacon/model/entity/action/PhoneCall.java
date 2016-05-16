package com.coska.beacon.model.entity.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.json.JSONObject;

public class PhoneCall extends Action {

/*
	{
		"dial_number": "1234567890"
*/

	public static final String DIAL_NUMBER = "dial_number";

	protected PhoneCall(JSONObject json) {
		super(json);
	}

	private String getDialNumber() {
		return json.optString(DIAL_NUMBER);
	}

	// Launch dial application as phone number being typed in, and let user decide whether to make a call or not.
	// We don't have to ask extra permission
	@Override
	public void perform(Context context) {
		Uri uri = Uri.fromParts("tel", getDialNumber(), null);
		context.startActivity(new Intent(Intent.ACTION_DIAL, uri));
	}
}