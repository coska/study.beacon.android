package com.coska.beacon.model.entity;

import android.provider.BaseColumns;

public class Rule implements BaseColumns {

	public enum Type {
		TIME, LOCATION;
	}

	public static final String _table = "rule";

	public static final String name = "name";
	public static final String type = "type";

	private Rule() { }
}