package com.coska.beacon.model.entity;

import android.content.Context;
import android.provider.BaseColumns;

public abstract class Rule implements BaseColumns {

	public enum Type {
		TIME, LOCATION;
	}

	public static final String _table = "rule";
	public static final String _task_id = "task_id";

	public static final String name = "name";
	public static final String type = "type";

	public abstract boolean isMatch(Context context);

	public static final class Time extends Rule {

		@Override
		public boolean isMatch(Context context) {
			// TODO: implement timestamp checking
			return false;
		}
	}

	public static final class Location extends Rule {

		@Override
		public boolean isMatch(Context context) {
			// TODO: implement location checking synchronously
			return false;
		}
	}
}