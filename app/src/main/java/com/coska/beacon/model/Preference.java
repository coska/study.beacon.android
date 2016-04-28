package com.coska.beacon.model;

import android.content.Context;
import android.content.SharedPreferences;

public final class Preference {

	private static final String PREFERENCE_NAME = "beacon.preference";

	public static Preference getInstance(Context context) {
		return new Preference(context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE));
	}

	private final SharedPreferences preferences;
	private Preference(SharedPreferences preferences) {
		this.preferences = preferences;
	}

	// TODO: implement getter, setter methods.
}