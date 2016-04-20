package com.coska.beacon.model.entity;

import android.provider.BaseColumns;

public class BeaconStatus implements BaseColumns {

	public static final String _table = "beacon_status";

	public static final String id = "id";
	public static final String status = "status";

	private BeaconStatus() { }
}