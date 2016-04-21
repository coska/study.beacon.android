package com.coska.beacon.model.entity.rule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.coska.beacon.model.BeaconProvider;
import com.coska.beacon.model.entity.EntityIterator;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Rule implements BaseColumns {

	public enum Type {
		Time, Location;
	}

	public static final class Builder {

		private String name = null;
		private Type type = null;
		private JSONObject json = new JSONObject();

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder type(Type type) {
			this.type = type;
			return this;
		}

		public Builder set(String key, int value) {
			try {
				json.put(key, value);
			} catch (JSONException e) {
				throw new IllegalArgumentException(e);
			}

			return this;
		}

		public Builder set(String key, String value) {
			try {
				json.put(key, value);
			} catch (JSONException e) {
				throw new IllegalArgumentException(e);
			}

			return this;
		}

		public ContentValues build() {
			ContentValues cv = new ContentValues(3);
			cv.put(Rule.name, name);
			cv.put(Rule.type, type.ordinal());
			cv.put(Rule.configuration, json.toString());
			return cv;
		}
	}

	public static final class Iterator extends EntityIterator<Rule> {

		public Iterator(Context context, Cursor cursor) {
			super(context, cursor);
		}

		@Override
		public Rule next(Cursor cursor) {
			return getInstance(cursor);
		}

		@Override
		protected Uri buildUriForDelete(long id) {
			return BeaconProvider.buildUri(BeaconProvider.PATH_RULE, id);
		}
	}

	public static Rule getInstance(Cursor cursor) {

		JSONObject json;
		try {
			json = new JSONObject(cursor.getString(cursor.getColumnIndex(configuration)));
		} catch (JSONException e) {
			json = new JSONObject();
		}

		final int value = cursor.getInt(cursor.getColumnIndex(type));
		switch (value) {
			case 1:
				return new Time(json);

			case 2:
				return new Location(json);

			default:
				throw new IllegalArgumentException("Unknown Rule type: " + value);
		}
	}

	public static final String _table = "rule";
	public static final String _task_id = "task_id";

	public static final String name = "name";
	public static final String type = "type";

	public static final String configuration = "configuration";

	protected final JSONObject json;
	protected Rule(JSONObject json) {
		this.json = json;
	}

	public abstract boolean isMatch(Context context);
}