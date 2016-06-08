package com.coska.beacon.model;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import java.util.Random;
import java.util.UUID;

public abstract class MockCursorLoader extends AsyncTaskLoader<Cursor> {

	protected final Random random = new Random();

	private Cursor cache;
	public MockCursorLoader(Context context) {
		super(context);
	}

	@Override
	public void deliverResult(Cursor cursor) {
		if (isReset()) {
			cursor.close();
			return;
		}

		Cursor old = cache;
		cache = cursor;

		if (isStarted()) {
			super.deliverResult(cursor);
		}

		if (old != null && old != cursor) {
			old.close();
		}
	}

	@Override
	protected void onStartLoading() {
		if (cache != null) {
			deliverResult(cache);
		}

		if (takeContentChanged() || cache == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	protected void onReset() {
		onStopLoading();

		if (cache != null) {
			cache.close();
			cache = null;
		}
	}

	@Override
	public void onCanceled(Cursor cursor) {
		super.onCanceled(cursor);
		cursor.close();
	}

	protected String getUUID() {
		return UUID.randomUUID().toString().toUpperCase();
	}

	protected String getString() {
		return Long.toString(Math.abs(random.nextLong()), 36).toUpperCase();
	}

	protected Long getLong() {
		return Math.abs(random.nextLong());
	}

	protected Double getDouble() {
		return Math.abs(random.nextDouble());
	}
}