package com.coska.beacon.model;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;

import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.model.entity.Signal;

import java.util.Random;

public class BeaconCursorLoader extends MockCursorLoader {

	private static final String[] columns = new String[] {
			Beacon._ID, Beacon.uuid, Beacon.name, Beacon.major, Beacon.minor,
			Signal.distance, Signal.telemetry, Signal.battery, Signal.pduCount, Signal.uptime, Signal.time
	};

	private int id = 0;

	public BeaconCursorLoader(Context context) {
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
				id++, getUUID(), "My Beacon " + getString(), getString(), getString(),
				getDouble(), getLong(), getLong(), getLong(), getLong(), getLong()
		};
	}
}