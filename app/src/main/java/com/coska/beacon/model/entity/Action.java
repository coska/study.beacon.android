package com.coska.beacon.model.entity;

import android.provider.BaseColumns;

public class Action implements BaseColumns {

	public enum Type {
		MESSAGE, PHONE_CALL, WIFI;
	}

	public static final String _table = "action";

	public static final String name = "name";
	public static final String type = "type";

	private Action() { }
}