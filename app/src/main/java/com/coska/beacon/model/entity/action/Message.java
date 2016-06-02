package com.coska.beacon.model.entity.action;

import android.content.Context;
import android.telephony.SmsManager;

import org.json.JSONObject;

public class Message extends Action {

	public static final String DIAL_NUMBER = "dial_number";
	public static final String SMS_MESSAGE = "sms_message";

	protected Message(JSONObject json) {
		super(json);
	}

	private String getDialNumber() {
		return json.optString(DIAL_NUMBER);
	}
	private String getSmsMessage() {
		return json.optString(SMS_MESSAGE);
	}

	@Override
	public void perform(Context context) {
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(getDialNumber(), null, getSmsMessage(), null, null);
	}
}
