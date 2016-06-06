package com.coska.beacon.model;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;

import com.coska.beacon.model.entity.rule.Rule;

public class RuleCursorLoader extends MockCursorLoader {

	private static final String[] columns = new String[] { Rule.name, Rule.type, Rule.configuration };

	public RuleCursorLoader(Context context) {
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
		return new Object[] { "My Rule " + getString(), getLong(), getString() };
	}
}