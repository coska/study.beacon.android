package com.coska.beacon.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

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

	private static final String BUNDLE_IDENTIFIER = "_bundle_identifier";

	public static void startService(Context context, String... identifier) {
		context.startService(new Intent(context, TaskService.class)
			.putExtra(BUNDLE_IDENTIFIER, identifier));
	}

	public TaskService() {
		super(TaskService.class.getSimpleName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		if(intent.hasExtra(BUNDLE_IDENTIFIER)) {
			validateBeacon(intent.getStringExtra(BUNDLE_IDENTIFIER));

		} else {
			validateBeacons();
		}
	}

	private void validateBeacon(String... identifier) {

		String where = "";
		switch (identifier.length) {
			default: where += " AND " + Beacon.identifier3 + "=?";
			case 2: where += " AND " + Beacon.identifier2 + "=?";
			case 1: where += " AND " + Beacon.identifier1 + "=?";
			case 0:
		}

		Uri beaconUri = BeaconProvider.buildUri(PATH_BEACON);
		Cursor cursor = getContentResolver().query(beaconUri, null, where.substring(4), identifier, null);
		for(int i = 0; cursor != null && cursor.moveToPosition(i); i++) {
			validateTasks(cursor.getLong(cursor.getColumnIndex(Beacon._ID)));
		}

		close(cursor);
	}

	private void validateBeacons() {

		Uri beaconUri = BeaconProvider.buildUri(PATH_BEACON);
		Cursor cursor = getContentResolver().query(beaconUri, null, null, null, null);
		for(int i = 0; cursor != null && cursor.moveToPosition(i); i++) {
			validateTasks(cursor.getLong(cursor.getColumnIndex(Beacon._ID)));
		}

		close(cursor);
	}

	private void validateTasks(long beaconId) {

		Uri taskUri = BeaconProvider.buildUri(PATH_BEACON, beaconId, PATH_TASK);
		Cursor cursor = getContentResolver().query(taskUri, null, null, null, null);
		for(int i = 0; cursor != null && cursor.moveToPosition(i); i++) {
			final long taskId = cursor.getLong(cursor.getColumnIndex(Task._ID));
			if(validateRules(taskId)) {
				validateActions(taskId);
			}
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