package com.coska.beacon.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.coska.beacon.model.entity.Action;

public class Database extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "beacon.database";
	public static final int DATABASE_VERSION = 1;

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE " + Action._table + " ("
				+ Action._ID + " INTEGER PRIMARY KEY, "
				+ Action._task_id + " INTEGER, "
				+ Action.name + " TEXT, "
				+ Action.type + " INTEGER);");

		// TODO: add all entities creation statement.
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}