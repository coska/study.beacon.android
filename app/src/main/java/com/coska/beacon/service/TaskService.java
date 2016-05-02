package com.coska.beacon.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.coska.beacon.model.BeaconProvider;
import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.model.entity.Task;
import com.coska.beacon.model.entity.action.Action;
import com.coska.beacon.model.entity.rule.Rule;

import static com.coska.beacon.model.BeaconProvider.PATH_ACTION;
import static com.coska.beacon.model.BeaconProvider.PATH_BEACON;
import static com.coska.beacon.model.BeaconProvider.PATH_RULE;
import static com.coska.beacon.model.BeaconProvider.PATH_TASK;

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
		if(TextUtils.isEmpty(uuid)) {
			validateBeacons();

		} else {
			validateBeacon(uuid);
		}
	}

	private void validateBeacon(String uuid) {

		Uri beaconUri = BeaconProvider.buildUri(PATH_BEACON, uuid);
		Cursor cursor = getContentResolver().query(beaconUri, null, null, null, null);
		for(int i = 0; cursor != null && cursor.moveToPosition(i); i++) {
			final long beaconId = cursor.getLong(cursor.getColumnIndex(Beacon._ID));
			// TODO: retrieve tasks and validate them.
			validateTasks(beaconId);
		}

		close(cursor);
	}

	private void validateBeacons() {

		Uri beaconUri = BeaconProvider.buildUri(PATH_BEACON);
		Cursor cursor = getContentResolver().query(beaconUri, null, null, null, null);
		for(int i = 0; cursor != null && cursor.moveToPosition(i); i++) {
			final long beaconId = cursor.getLong(cursor.getColumnIndex(Beacon._ID));
			// TODO: retrieve tasks and validate them.
			validateTasks(beaconId);
		}

		close(cursor);
	}

	private void validateTasks(long beaconId) {

		Uri taskUri = BeaconProvider.buildUri(PATH_BEACON, beaconId, PATH_TASK);
		Cursor cursor = getContentResolver().query(taskUri, null, null, null, null);
		for(int i = 0; cursor != null && cursor.moveToPosition(i); i++) {
			final long taskId = cursor.getLong(cursor.getColumnIndex(Task._ID));
			// TODO: validate rules associated with the task id, and execute actions only if all rules are qualified.
			if(validateRules(taskId) == true)
			{
				validateActions(taskId);
			}
			// What to do about location ???
		}

		close(cursor);
	}

	private boolean validateRules(long taskId) {

		Uri ruleUri = BeaconProvider.buildUri(PATH_TASK, taskId, PATH_RULE);
		Cursor cursor = null;
		try {
			cursor = getContentResolver().query(ruleUri, null, null, null, null);
			Rule.Iterator iterator = new Rule.Iterator(this, cursor);
			while (iterator.hasNext()) {
				if(!iterator.next().isMatch(this)) {
					return false;
				}
			}

		} finally {
			close(cursor);
		}

		return true;
	}

	private void validateActions(long taskId) {

		Uri actionUri = BeaconProvider.buildUri(PATH_TASK, taskId, PATH_ACTION);
		Cursor cursor = null;
		try {
			cursor = getContentResolver().query(actionUri, null, null, null, null);
			Action.Iterator iterator = new Action.Iterator(this, cursor);
			while (iterator.hasNext()) {
				iterator.next().perform(this);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			close(cursor);
		}
	}

	private void close(Cursor cursor) {
		if(cursor != null) {
			cursor.close();
		}
	}
}