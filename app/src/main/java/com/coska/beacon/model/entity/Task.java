package com.coska.beacon.model.entity;

import android.provider.BaseColumns;

public class Task implements BaseColumns {

	public static final String _table = "task";

	public static final String name = "name";
	public static final String rule = "rule";
	public static final String action = "action";
	public static final String beacon = "beacon";

	private Task() { }
}