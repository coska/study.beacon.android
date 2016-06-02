package com.coska.beacon.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.coska.beacon.model.entity.Signal;
import com.coska.beacon.model.entity.action.Action;
import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.model.entity.rule.Rule;
import com.coska.beacon.model.entity.Task;

public class Database extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "beacon.database";
	public static final int DATABASE_VERSION = 1;

	public Database(Context context) {
		this(context, DATABASE_NAME);
	}

	protected Database(Context context, String name) {
		super(context, name, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE " + Signal._table + " ("
				+ Signal.time + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
				+ Signal.uuid + " TEXT NOT NULL, "
				+ Signal.major + " TEXT DEFAULT '', "
				+ Signal.minor + " TEXT DEFAULT '', "
				+ Signal.distance + " REAL, "
				+ Signal.telemetry + " INTEGER, "
				+ Signal.battery + " INTEGER, "
				+ Signal.pduCount + " INTEGER, "
				+ Signal.uptime + " INTEGER, "
				+ " UNIQUE (" + Signal.uuid + ", " + Signal.major + ", " + Signal.minor + ") ON CONFLICT REPLACE);");

		db.execSQL("CREATE TABLE " + Beacon._table + " ("
				+ Beacon._ID + " INTEGER PRIMARY KEY, "
				+ Beacon.uuid + " TEXT NOT NULL UNIQUE, "
				+ Beacon.name + " TEXT, "
				+ Beacon.major + " TEXT, "
				+ Beacon.minor + " TEXT, "
				+ Beacon.status + " TEXT);");

		db.execSQL("CREATE TABLE " + Task._table + " ("
				+ Task._ID + " INTEGER PRIMARY KEY, "
				+ Task._beacon_id + " INTEGER NOT NULL, "
//				+ Task.name + " TEXT);");
				+ Task.name + " TEXT, "
				+ " FOREIGN KEY(" + Task._beacon_id + ")"
				+ " REFERENCES " + Beacon._table + "(" + Beacon._ID + ") ON DELETE CASCADE);");

		db.execSQL("CREATE TABLE " + Action._table + " ("
				+ Action._ID + " INTEGER PRIMARY KEY, "
				+ Action._task_id + " INTEGER NOT NULL, "
				+ Action.name + " TEXT, "
				+ Action.type + " INTEGER NOT NULL, "
//				+ Action.configuration + " TEXT);");
				+ Action.configuration + " TEXT, "
				+ " FOREIGN KEY(" + Action._task_id + ")"
				+ " REFERENCES " + Task._table + "(" + Task._ID + ") ON DELETE CASCADE);");

		db.execSQL("CREATE TABLE " + Rule._table + " ("
				+ Rule._ID + " INTEGER PRIMARY KEY, "
				+ Rule._task_id + " INTEGER NOT NULL, "
				+ Rule.name + " TEXT, "
				+ Rule.type + " INTEGER NOT NULL, "
//				+ Rule.configuration + " TEXT);");
				+ Rule.configuration + " TEXT, "
				+ " FOREIGN KEY(" + Rule._task_id + ")"
				+ " REFERENCES " + Task._table + "(" + Task._ID + ") ON DELETE CASCADE);");

//		db.execSQL("CREATE TRIGGER " + Beacon._table + "_trigger BEFORE DELETE ON " + Beacon._table
//				+ " FOR EACH ROW BEGIN "
//					+ " DELETE FROM " + Task._table + " WHERE OLD." + Beacon._ID + "=" + Task._table + "." + Task._beacon_id
//				+ "; END;");

//		db.execSQL("CREATE TRIGGER " + Task._table + "_trigger BEFORE DELETE ON " + Task._table
//				+ " FOR EACH ROW BEGIN "
//				+ " DELETE FROM " + Action._table + " WHERE OLD." + Task._ID + "=" + Action._table + "." + Action._task_id + ";"
//				+ " DELETE FROM " + Rule._table + " WHERE OLD." + Task._ID + "=" + Rule._table + "." + Rule._task_id + ";"
//				+ " END;");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

	@Override
	public void onConfigure(SQLiteDatabase db) {
		db.setForeignKeyConstraintsEnabled(true);
	}
}