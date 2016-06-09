package com.coska.beacon.model;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;

import com.coska.beacon.model.entity.action.Action;

public class ActionCursorLoader extends MockCursorLoader {

	private static final String[] columns = new String[] { Action.type, Action.configuration };

	public ActionCursorLoader(Context context) {
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
		return new Object[] { random.nextInt(3), getString() };
	}
}