package com.coska.beacon.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.coska.beacon.model.entity.Action;

public class BeaconProvider extends ContentProvider {

	public static final String SCHEME = "content://";
	public static final String AUTHORITY = "com.coska.beacon.provider";

	public static final String PATH_ACTION = "action";

	private static final int ACTION = 1;
	private static final int ACTION_ID = 2;

	private static final UriMatcher sUriMatcher;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, PATH_ACTION, ACTION);
		sUriMatcher.addURI(AUTHORITY, PATH_ACTION + "/#", ACTION_ID);
	}

	private SQLiteOpenHelper mOpenHelper;

	@Override
	public boolean onCreate() {
		mOpenHelper = new Database(getContext());
		return true;
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
			case ACTION:
				builder.setTables(Action._table);
				break;

			case ACTION_ID:
				builder.setTables(Action._table);
				builder.appendWhere(Action._ID + "=" + uri.getPathSegments().get(1));
				break;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		Cursor cursor = builder.query(mOpenHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Nullable
	@Override
	public String getType(@NonNull Uri uri) {

		switch (sUriMatcher.match(uri)) {

			case ACTION:
				return "vnd.android.cursor.dir/com.coska.beacon.action";

			case ACTION_ID:
				return "vnd.android.cursor.item/com.coska.beacon.action";

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, ContentValues values) {

		final long rowId;
		switch (sUriMatcher.match(uri)) {
			case ACTION: {
				rowId = mOpenHelper.getWritableDatabase().insert(Action._table, null, values);
				break;
			}

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		if (0 < rowId) {
			Uri actionUri = Uri.parse(SCHEME + AUTHORITY + "/action/" + rowId);
			getContext().getContentResolver().notifyChange(actionUri, null);

			return actionUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(@NonNull Uri uri, String whereClause, String[] whereArgs) {

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();

		final int count;
		switch (sUriMatcher.match(uri)) {
			case ACTION:
				count = db.delete(Action._table, whereClause, whereArgs);
				break;

			case ACTION_ID:
				StringBuilder where = new StringBuilder()
						.append(Action._ID).append("=").append(uri.getPathSegments().get(1));

				if(!TextUtils.isEmpty(whereClause)) {
					where.append(" AND ");
				}

				count = db.delete(Action._table, where.toString(), whereArgs);
				break;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

	@Override
	public int update(@NonNull Uri uri, ContentValues values, String whereClause, String[] whereArgs) {

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();

		final int count;
		switch (sUriMatcher.match(uri)) {
			case ACTION:
				count = db.update(Action._table, values, whereClause, whereArgs);
				break;

			case ACTION_ID:
				StringBuilder where = new StringBuilder()
						.append(Action._ID).append("=").append(uri.getPathSegments().get(1));

				if(!TextUtils.isEmpty(whereClause)) {
					where.append(" AND ");
				}

				count = db.update(Action._table, values, where.toString(), whereArgs);
				break;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}
}