package com.coska.beacon.model.entity;

import android.provider.BaseColumns;

public class Signal implements BaseColumns {

	public static final String _table = "signal";

	public static final String identifier1 = "identifier_1";
	public static final String identifier2 = "identifier_2";
	public static final String identifier3 = "identifier_3";

	public static final String name = "name";
	public static final String distance = "distance";
	public static final String telemetry = "telemetry";
	public static final String battery = "battery";
	public static final String pduCount = "pdu_count";
	public static final String uptime = "uptime";

	public static final String time = "time";

	private Signal() { }
}