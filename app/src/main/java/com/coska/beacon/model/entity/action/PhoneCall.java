package com.coska.beacon.model.entity.action;

import android.content.Context;

import org.json.JSONObject;

public class PhoneCall extends Action {

	protected PhoneCall(JSONObject json) {
		super(json);
	}

	@Override
	public void perform(Context context) {
		// TODO: open a phone-call app and dial in
	}
}