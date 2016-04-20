package com.coska.beacon.model.entity.action;

import android.content.Context;

import org.json.JSONObject;

public class Message extends Action {

	protected Message(JSONObject json) {
		super(json);
	}

	@Override
	public void perform(Context context) {
		// TODO: send text message
	}
}