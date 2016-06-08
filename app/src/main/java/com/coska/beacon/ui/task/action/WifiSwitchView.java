package com.coska.beacon.ui.task.action;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Switch;

import com.coska.beacon.R;

import org.json.JSONException;
import org.json.JSONObject;

public class WifiSwitchView extends ActionView {

	private Switch wifiSwitch;

	public WifiSwitchView(Context context) {
		super(context);
	}

	public WifiSwitchView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WifiSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		wifiSwitch = (Switch) findViewById(R.id.wifi_switch);
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public String getConfiguration() {
		try {
			return new JSONObject()
					.put("switch", wifiSwitch.isChecked())
					.toString();

		} catch (JSONException ignore) {
			return "";
		}
	}
}