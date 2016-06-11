package com.coska.beacon.model.entity;

import android.provider.BaseColumns;

public class Beacon implements BaseColumns {

	public static final String _table = "beacon";

	public static final String name = "name";
	public static final String identifier1 = "identifier_1";
	public static final String identifier2 = "identifier_2";
	public static final String identifier3 = "identifier_3";

	private Beacon() { }
}