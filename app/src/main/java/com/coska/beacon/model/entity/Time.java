package com.coska.beacon.model.entity;

import android.provider.BaseColumns;

public class Time implements BaseColumns {

	public enum Type {
		DAY_OF_WEEK, TIME_OF_DAY, WEEK_OF_MONTH, START_TIME, END_TIME;
	}

	public static final String _table = "time";

	public static final String name = "name";
	public static final String time = "time";

	private Time() { }
}