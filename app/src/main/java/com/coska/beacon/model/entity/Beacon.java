package com.coska.beacon.model.entity;

import android.provider.BaseColumns;

public class Beacon implements BaseColumns {

	public static final String _table = "beacon";

	public static final String uuid = "uuid";
	public static final String name = "name";

	public static final String major = "major";
	public static final String minor = "minor";

	private Beacon() { }
}