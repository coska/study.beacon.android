package com.coska.beacon.model.entity;

import android.content.Context;
import android.provider.BaseColumns;

public abstract class Action implements BaseColumns {

	public enum Type {
		MESSAGE, PHONE_CALL, WIFI;
	}

	public static final String _table = "action";
	public static final String _task_id = "task_id";

	public static final String name = "name";
	public static final String type = "type";

	public static final String configuration = "configuration";

	public abstract void perform(Context context);

	private static final class ActionMessage extends Action {

		@Override
		public void perform(Context context) {
			// TODO: send text message
		}
	}

	private static final class ActionPhoneCall extends Action {

		@Override
		public void perform(Context context) {
			// TODO: open a phone-call app and dial in
		}
	}

	private static final class ActionWifi extends Action {

		@Override
		public void perform(Context context) {
			// TODO: turn on or off wifi connection
		}
	}
}