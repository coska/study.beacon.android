package com.coska.beacon.ui.task.action;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import com.coska.beacon.R;
import com.coska.beacon.model.entity.action.PhoneCall;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneCallView extends ActionView {

	private EditText name, number;
	private JSONObject json;

	public PhoneCallView(Context context) {
		super(context);
	}

	public PhoneCallView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PhoneCallView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		name = (EditText) findViewById(R.id.name);
		number = (EditText) findViewById(R.id.number);
	}

	@Override
	public boolean validate() {
		return !(TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(number.getText()));
	}

	@Override
	public void setConfiguration(String configuration) throws JSONException {
		json = new JSONObject(configuration);
		name.setText(json.optString(PhoneCall.NAME));
		number.setText(json.optString(PhoneCall.DIAL_NUMBER));
	}

	@Override
	public String getConfiguration() {
		try {
			return (json == null ? new JSONObject() : json)
					.put(PhoneCall.NAME, name.getText().toString())
					.put(PhoneCall.DIAL_NUMBER, number.getText().toString())
					.toString();

		} catch (JSONException ignore) {
			return "";
		}
	}
}