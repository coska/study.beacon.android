package com.coska.beacon.model.entity.action;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.coska.beacon.model.BeaconProvider;
import com.coska.beacon.model.entity.EntityIterator;
import com.coska.beacon.model.entity.rule.Location;
import com.coska.beacon.model.entity.rule.Time;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Action implements BaseColumns {

	public enum Type {
		MESSAGE, PHONE_CALL, WIFI;
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
			cv.put(Action.name, name);
			cv.put(Action.type, type.ordinal());
			cv.put(Action.configuration, json.toString());
			return cv;
		}
	}

	public static final class Iterator extends EntityIterator<Action> {

		public Iterator(Context context, Cursor cursor) {
			super(context, cursor);
		}

		@Override
		protected Action next(Cursor cursor) {
			return getInstance(cursor);
		}

		@Override
		protected Uri buildUriForDelete(long id) {
			return BeaconProvider.buildUri(BeaconProvider.PATH_ACTION, id);
		}
	}

	public static Action getInstance(Cursor cursor) {

		JSONObject json;
		try {
			json = new JSONObject(cursor.getString(cursor.getColumnIndex(configuration)));
		} catch (JSONException e) {
			json = new JSONObject();
		}

		final int value = cursor.getInt(cursor.getColumnIndex(type));
		switch (value) {
			case 1:
				return new Message(json);

			case 2:
				return new PhoneCall(json);

			case 3:
				return new Wifi(json);

			default:
				throw new IllegalArgumentException("Unknown Rule type: " + value);
		}
	}

	public static final String _table = "action";
	public static final String _task_id = "task_id";

	public static final String name = "name";
	public static final String type = "type";

	public static final String configuration = "configuration";

	protected final JSONObject json;
	protected Action(JSONObject json) {
		this.json = json;
	}

	public abstract void perform(Context context);
}