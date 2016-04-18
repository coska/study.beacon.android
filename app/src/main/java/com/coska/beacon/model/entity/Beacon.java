package com.coska.beacon.model.entity;

import android.provider.BaseColumns;

public class Beacon implements BaseColumns {

	public static final String _table = "beacon";

	public static final String name = "name";
	public static final String id = "id";

	private Beacon() { }
}