package com.coska.beacon.ui.task.action;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import com.coska.beacon.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageView extends ActionView {

	private EditText name, number, text;

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
	}

	@Override
	public boolean validate() {
		return !(TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(number.getText()) || TextUtils.isEmpty(text.getText()));
	}

	@Override
	public String getConfiguration() {
		try {
			return new JSONObject()
					.put("name", name.getText().toString())
					.put("number", number.getText().toString())
					.put("text", text.getText().toString())
					.toString();

		} catch (JSONException ignore) {
			return "";
		}
	}
}