package com.coska.beacon.model.entity.rule;

import android.content.Context;

import org.json.JSONObject;

import java.util.Calendar;

public class Time extends Rule {

	public static final String MONDAY = Integer.toString(Calendar.MONDAY);
	public static final String TUESDAY = Integer.toString(Calendar.TUESDAY);
	public static final String WEDNESDAY = Integer.toString(Calendar.WEDNESDAY);
	public static final String THURSDAY = Integer.toString(Calendar.THURSDAY);
	public static final String FRIDAY = Integer.toString(Calendar.FRIDAY);
	public static final String SATURDAY = Integer.toString(Calendar.SATURDAY);
	public static final String SUNDAY = Integer.toString(Calendar.SUNDAY);

	public static final String[] _WEEK = new String[] { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY };

	protected Time(JSONObject json) {
		super(json);
	}

	@Override
	public boolean isMatch(Context context) {

		Calendar calendar = Calendar.getInstance();
		String key = Integer.toString(calendar.get(Calendar.DAY_OF_WEEK));
		if(json.has(key)) {
			String[] array = json.optString(key).split("[^0-9]");
			int time = calendar.get(Calendar.HOUR_OF_DAY)*100 + calendar.get(Calendar.MINUTE);
			return Integer.parseInt(array[0]) < time && time < Integer.parseInt(array[1]);
		}

		return false;
	}
}