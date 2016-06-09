package com.coska.beacon.ui.task.action;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import com.coska.beacon.R;
import com.coska.beacon.model.entity.action.Message;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageView extends ActionView {

	private EditText name, number, text;
	private JSONObject json;

	public MessageView(Context context) {
		super(context);
	}

	public MessageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		name = (EditText) findViewById(R.id.name);
		number = (EditText) findViewById(R.id.number);
		text = (EditText) findViewById(R.id.text);

		if(json != null) {
			name.setText(json.optString(Message.NAME));
			number.setText(json.optString(Message.DIAL_NUMBER));
			text.setText(json.optString(Message.SMS_MESSAGE));
		}
	}

	@Override
	public boolean validate() {
		return !(TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(number.getText()) || TextUtils.isEmpty(text.getText()));
	}

	@Override
	public void setConfiguration(String configuration) throws JSONException {
		json = new JSONObject(configuration);
		name.setText(json.optString(Message.NAME));
		number.setText(json.optString(Message.DIAL_NUMBER));
		text.setText(json.optString(Message.SMS_MESSAGE));
	}

	@Override
	public String getConfiguration() {
		try {
			return (json == null ? new JSONObject() : json)
					.put(Message.NAME, name.getText().toString())
					.put(Message.DIAL_NUMBER, number.getText().toString())
					.put(Message.SMS_MESSAGE, text.getText().toString())
					.toString();

		} catch (JSONException ignore) {
			return "";
		}
	}
}