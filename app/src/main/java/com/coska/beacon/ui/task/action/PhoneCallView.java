package com.coska.beacon.ui.task.action;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import com.coska.beacon.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneCallView extends ActionView {

	private EditText name, number;

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
	public String getConfiguration() {
		try {
			return new JSONObject()
					.put("name", name.getText().toString())
					.put("number", number.getText().toString())
					.toString();

		} catch (JSONException ignore) {
			return "";
		}
	}

}