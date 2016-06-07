package com.coska.beacon.model;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;

import com.coska.beacon.model.entity.Task;

public class TaskCursorLoader extends MockCursorLoader {

	private static final String[] columns = new String[] { BaseColumns._ID, Task.name };

	private int id = 0;

	public TaskCursorLoader(Context context) {
		super(context);
	}

	@Override
	public Cursor loadInBackground() {

		MatrixCursor cursor = new MatrixCursor(columns);
		cursor.addRow(getRow());
		cursor.addRow(getRow());
		cursor.addRow(getRow());
		cursor.addRow(getRow());
		cursor.addRow(getRow());

		return cursor;
	}

	private Object[] getRow() {
		return new Object[] { id++, "My Task " + getString() };
	}
}