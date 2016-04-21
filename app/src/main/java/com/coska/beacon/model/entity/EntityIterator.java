package com.coska.beacon.model.entity;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.io.Closeable;
import java.util.Iterator;

public abstract class EntityIterator<E> implements Iterator<E>, Closeable {

	private final Context context;
	private final Cursor cursor;

	protected abstract E next(Cursor cursor);
	protected abstract Uri buildUriForDelete(long id);

	public EntityIterator(Context context, Cursor cursor) {
		this.context = context;
		this.cursor = cursor;
	}

	@Override
	public boolean hasNext() {
		return cursor.getPosition() < cursor.getCount();
	}

	@Override
	public E next() {

		if(cursor.moveToNext()) {
			return next(cursor);

		} else {
			throw new IllegalStateException("Cursor reached end");
		}
	}

	@Override
	public void remove() {
		final long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
		context.getContentResolver().delete(buildUriForDelete(id), null, null);
	}

	@Override
	public void close() {
		cursor.close();
	}
}