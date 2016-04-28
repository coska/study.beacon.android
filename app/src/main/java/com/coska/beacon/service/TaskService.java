package com.coska.beacon.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import static com.coska.beacon.model.BeaconProvider.AUTHORITY;
import static com.coska.beacon.model.BeaconProvider.PATH_ACTION;
import static com.coska.beacon.model.BeaconProvider.SCHEME;

public class TaskService extends IntentService {

	private static final String BUNDLE_UUID = "_bundle_uuid";

	public static void startService(Context context, String uuid) {
		context.startService(new Intent(context, TaskService.class)
			.putExtra(BUNDLE_UUID, uuid));
	}

	public TaskService() {
		super(TaskService.class.getSimpleName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		String uuid = intent.getStringExtra(BUNDLE_UUID);

		// TODO: retrieve entities and perform evaluation.
		Uri actionUri = Uri.parse(SCHEME + AUTHORITY + "/" + PATH_ACTION);
		Cursor cursor = getContentResolver().query(actionUri, null, null, null, null);
		cursor.close();
	}
}