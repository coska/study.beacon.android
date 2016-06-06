package com.coska.beacon.model;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;

import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.model.entity.Signal;

public class SignalCursorLoader extends MockCursorLoader {

	private static final String[] columns = new String[] {
			Signal.uuid, Signal.major, Signal.minor, Signal.distance, Signal.telemetry, Signal.battery, Signal.pduCount, Signal.uptime, Signal.time
	};

	public SignalCursorLoader(Context context) {
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
		return new Object[] {
				getUUID(), getString(), getString(), getDouble(), getLong(), getLong(), getLong(), getLong(), getLong()
		};
	}
}