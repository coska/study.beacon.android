package com.coska.beacon.model.entity;

import android.provider.BaseColumns;

public class Time implements BaseColumns {

	public enum Type {
		ONCE, REPEAT_ONCE_A_DAY, REPEAT_DAY_OF_WEEK, REPEAT_DAY_OF_MONTH;
	}

	public static final String _table = "time";

	public static final String name = "name";
	public static final String type = "type";
	public static final String begin = "range_begin";
	public static final String rangeEnd = "range_end";

	private Time() { }
}