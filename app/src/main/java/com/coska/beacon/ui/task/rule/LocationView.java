package com.coska.beacon.ui.task.rule;

import android.content.Context;
import android.util.AttributeSet;

import org.json.JSONException;

public class LocationView extends RuleView {

	public LocationView(Context context) {
		super(context);
	}

	public LocationView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LocationView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public void setConfiguration(String configuration) throws JSONException {

	}

	@Override
	public String getConfiguration() {
		return null;
	}
}