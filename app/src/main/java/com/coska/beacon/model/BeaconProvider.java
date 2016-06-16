package com.coska.beacon.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.model.entity.Signal;
import com.coska.beacon.model.entity.Task;
import com.coska.beacon.model.entity.action.Action;
import com.coska.beacon.model.entity.rule.Rule;

public class BeaconProvider extends ContentProvider {

	public static final String SCHEME = "content://";
	private static final String BASE_PATH = "com.coska.beacon";
	public static final String AUTHORITY = BASE_PATH + ".provider";

	private static final String TYPE_ITEM = "vnd.android.cursor.item/" + BASE_PATH;
	private static final String TYPE_DIR = "vnd.android.cursor.dir/" + BASE_PATH;

	public static final String PATH_SIGNAL = "signal";
	public static final String PATH_BEACON = "beacon";
	public static final String PATH_TASK = "task";
	public static final String PATH_RULE = "rule";
	public static final String PATH_ACTION = "action";

	private static final int SIGNAL = 1;

	private static final int BEACON = 2;
	private static final int BEACON_ID = 3;
	private static final int BEACON_TASK = 4;

	private static final int TASK = 5;
	private static final int TASK_ID = 6;

	private static final int TASK_RULE = 7;
	private static final int TASK_ACTION = 8;

	private static final int RULE_ID = 9;
	private static final int ACTION_ID = 10;

	private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		matcher.addURI(AUTHORITY, PATH_SIGNAL, SIGNAL);

		matcher.addURI(AUTHORITY, PATH_BEACON, BEACON); // all beacons
		matcher.addURI(AUTHORITY, PATH_BEACON + "/#", BEACON_ID);   // a beacon with id
		matcher.addURI(AUTHORITY, PATH_BEACON + "/#/" + PATH_TASK, BEACON_TASK);    // all tasks associate with a beacon

		matcher.addURI(AUTHORITY, PATH_TASK, TASK); // all tasks
		matcher.addURI(AUTHORITY, PATH_TASK + "/#", TASK_ID);   // a task with id

		matcher.addURI(AUTHORITY, PATH_TASK + "/#/" + PATH_RULE, TASK_RULE);  // all rules associate with a task
		matcher.addURI(AUTHORITY, PATH_TASK + "/#/" + PATH_ACTION, TASK_ACTION);  // all actions associate with a task

		matcher.addURI(AUTHORITY, PATH_RULE + "/#", RULE_ID);    // a rule with id
		matcher.addURI(AUTHORITY, PATH_ACTION + "/#", ACTION_ID);    // an action with id
	}

	public static Uri buildUri(Object... segments) {
		StringBuilder builder = new StringBuilder()
				.append(SCHEME).append(AUTHORITY);

		for(Object segment:segments) {
			builder.append("/").append(segment.toString());
		}

		return Uri.parse(builder.toString());
	}

	protected SQLiteDatabase database;

	@Override
	public boolean onCreate() {
		database = new Database(getContext()).getWritableDatabase();
		return database != null;
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		switch (matcher.match(uri)) {
			case SIGNAL:
				builder.setTables(Signal._table);
				break;

			case BEACON:
				builder.setTables(Beacon._table);
				break;

			case BEACON_ID:
				builder.setTables(Beacon._table);
				builder.appendWhere(Beacon._ID + "=" + uri.getPathSegments().get(1));
				break;

			case BEACON_TASK:
				builder.setTables(Task._table);
				builder.appendWhere(Task._beacon_id + "=" + uri.getPathSegments().get(1));
				break;

			case TASK:
				builder.setTables(Task._table + "_view");
				break;

			case TASK_ID:
				builder.setTables(Task._table + "_view");
				builder.appendWhere(Task._ID + "=" + uri.getPathSegments().get(1));
				break;

			case TASK_RULE:
				builder.setTables(Rule._table);
				builder.appendWhere(Rule._task_id + "=" + uri.getPathSegments().get(1));
				break;

			case TASK_ACTION:
				builder.setTables(Action._table);
				builder.appendWhere(Action._task_id + "=" + uri.getPathSegments().get(1));
				break;

			case RULE_ID:
				builder.setTables(Rule._table);
				builder.appendWhere(Rule._ID + "=" + uri.getPathSegments().get(1));
				break;

			case ACTION_ID:
				builder.setTables(Action._table);
				builder.appendWhere(Action._ID + "=" + uri.getPathSegments().get(1));
				break;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		Cursor cursor = builder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Nullable
	@Override
	public String getType(@NonNull Uri uri) {

		switch (matcher.match(uri)) {
			case SIGNAL: return TYPE_DIR + "." + PATH_SIGNAL;
			case BEACON: return TYPE_DIR + "." + PATH_BEACON;
			case BEACON_ID: return TYPE_ITEM + "." + PATH_BEACON;
			case BEACON_TASK: return TYPE_ITEM + "." + PATH_TASK;
			case TASK: return TYPE_DIR + "." + PATH_TASK;
			case TASK_ID: return TYPE_ITEM + "." + PATH_TASK;
			case TASK_RULE: return TYPE_DIR + "." + PATH_RULE;
			case TASK_ACTION: return TYPE_DIR + "." + PATH_ACTION;
			case RULE_ID: return TYPE_ITEM + "." + PATH_RULE;
			case ACTION_ID: return TYPE_ITEM + "." + PATH_ACTION;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, ContentValues values) {

		switch (matcher.match(uri)) {
			case SIGNAL:
				if(0 <= database.insert(Signal._table, null, values)) {
					getContext().getContentResolver().notifyChange(uri, null);
				}
				return uri;

			case BEACON:
				return insertNotify(database.insert(Beacon._table, null, values), PATH_BEACON);

			case BEACON_TASK:
				values.put(Task._beacon_id, uri.getPathSegments().get(1));
				return insertNotify(database.insert(Task._table, null, values), PATH_TASK);

			case TASK_RULE:
				values.put(Rule._task_id, uri.getPathSegments().get(1));
				return insertNotify(database.insert(Rule._table, null, values), PATH_RULE);

			case TASK_ACTION:
				values.put(Action._task_id, uri.getPathSegments().get(1));
				return insertNotify(database.insert(Action._table, null, values), PATH_ACTION);

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	private Uri insertNotify(long rowId, String path) {

		Uri uri = Uri.parse(SCHEME + AUTHORITY + "/" + path + "/" + rowId);
		if(0 <= rowId) {
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return uri;
	}

	@Override
	public int delete(@NonNull Uri uri, String whereClause, String[] whereArgs) {

		switch (matcher.match(uri)) {

			case BEACON_ID:
				return delete(Beacon._table, uri);

			case TASK_ID:
				return delete(Task._table, uri);

			case RULE_ID:
				return delete(Rule._table, uri);

			case ACTION_ID:
				return delete(Action._table, uri);

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	private int delete(String table, Uri uri) {

		final int count = database.delete(table, BaseColumns._ID + "=?", new String[] { uri.getPathSegments().get(1) });
		if(0 < count) {
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return count;
	}

	@Override
	public int update(@NonNull Uri uri, ContentValues values, String whereClause, String[] whereArgs) {

		switch (matcher.match(uri)) {

			case BEACON_ID:
				return update(Beacon._table, values, uri);

			case TASK_ID:
				return update(Task._table, values, uri);

			case RULE_ID:
				return update(Rule._table, values, uri);

			case ACTION_ID:
				return update(Action._table, values, uri);

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	private int update(String table, ContentValues values, Uri uri) {

		final int count = database.update(table, values, BaseColumns._ID + "=?", new String[] { uri.getPathSegments().get(1) });
		if(0 < count) {
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return count;
	}
}