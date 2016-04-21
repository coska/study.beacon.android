package com.coska.beacon.model.entity;

import android.provider.BaseColumns;

public class Task implements BaseColumns {

	public static final String _table = "task";
	public static final String _beacon_id = "beacon_id";

	public static final String name = "name";

	private Task() { }
}