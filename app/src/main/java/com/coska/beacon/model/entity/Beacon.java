package com.coska.beacon.model.entity;

import android.provider.BaseColumns;

public class Beacon implements BaseColumns {

	public static final String _table = "beacon";

	public static final String name = "name";
	public static final String id = "id";
	public static final String major = "major";
	public static final String minor = "minor";
	public static final String battery = "battery";

	private Beacon() { }
}