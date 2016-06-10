package com.coska.beacon.ui.base;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.coska.beacon.R;
import com.coska.beacon.model.BeaconProvider;
import com.coska.beacon.model.Database;
import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.model.entity.Signal;
import com.coska.beacon.model.entity.Task;
import com.coska.beacon.model.entity.action.Action;
import com.coska.beacon.model.entity.action.Message;
import com.coska.beacon.model.entity.action.PhoneCall;
import com.coska.beacon.model.entity.action.Wifi;
import com.coska.beacon.model.entity.rule.Location;
import com.coska.beacon.model.entity.rule.Rule;
import com.coska.beacon.model.entity.rule.Time;

import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class BaseActivity extends AppCompatActivity {

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.database:

				Random random = new Random();

				Database database = new Database(this);
				SQLiteDatabase db = database.getWritableDatabase();

				db.beginTransaction();
				try {
					db.delete(Signal._table, null, null);
					db.delete(Beacon._table, null, null);

					for(int i = 0, beaconSize = random.nextInt(5)+5; i < beaconSize; i++) {
						ContentValues beacon = new ContentValues();
						beacon.put(Beacon.uuid, UUID.randomUUID().toString().toUpperCase());
						beacon.put(Beacon.name, "My Beacon " + getName(random));
						beacon.put(Beacon.major, Integer.toString(Math.abs(random.nextInt())));
						beacon.put(Beacon.minor, Integer.toString(Math.abs(random.nextInt())));

						final long beaconId = db.insert(Beacon._table, null, beacon);

						beacon.put(Signal.name, "Signal " + getName(random));
						beacon.put(Signal.distance, Math.abs(random.nextDouble()));
						beacon.put(Signal.telemetry, random.nextInt(10)+1);
						beacon.put(Signal.battery, Math.abs(random.nextLong()));
						beacon.put(Signal.pduCount, Math.abs(random.nextLong()));
						beacon.put(Signal.uptime, Math.abs(random.nextLong()));
						db.insert(Signal._table, null, beacon);

						for(int j = 0, taskSize = random.nextInt(5)+5; j < taskSize; j++) {
							ContentValues task = new ContentValues();
							task.put(Task._beacon_id, beaconId);
							task.put(Task.name, "My Task " + getName(random));

							final long taskId = db.insert(Task._table, null, task);

							for(int k = 0, ruleSize = random.nextInt(2)+2; k < ruleSize; k++) {

								Rule.Builder builder = new Rule.Builder();
								switch (Rule.Type.values()[random.nextInt(2)]) {
									case Time:
										builder.type(Rule.Type.Time);
										for(String day:Time._WEEK) {
											if(random.nextBoolean()) {
												builder.set(day, formatTime(random.nextInt(24)*100 + random.nextInt(60), random.nextInt(24)*100 + random.nextInt(60)));
											}
										}
										break;

									case Location:
										builder.type(Rule.Type.Location)
												.set(Location.LONGITUDE, Double.toString(random.nextDouble()))
												.set(Location.LATITUDE, Double.toString(random.nextDouble()))
												.set(Location.ACCURACY, Integer.toString(Math.abs(random.nextInt(1000))));
										break;
								}

								db.insert(Rule._table, null, builder.build(taskId));
							}

							for(int k = 0, actionSize = random.nextInt(2)+2; k < actionSize; k++) {

								Action.Builder builder = new Action.Builder();
								switch (Action.Type.values()[random.nextInt(3)]) {
									case MESSAGE:
										builder.type(Action.Type.MESSAGE)
												.set(Message.NAME, "My Message " + getName(random))
												.set(Message.DIAL_NUMBER, String.format(Locale.US, "%09d", random.nextInt(1000000000)))
												.set(Message.SMS_MESSAGE, Long.toString(Math.abs(random.nextLong()), 36));
										break;

									case PHONE_CALL:
										builder.type(Action.Type.PHONE_CALL)
												.set(PhoneCall.NAME, "My Phone call " + getName(random))
												.set(PhoneCall.DIAL_NUMBER, String.format(Locale.US, "%09d", random.nextInt(1000000000)));
										break;

									case WIFI:
										builder.type(Action.Type.WIFI)
												.set(Wifi.WIFI_STATUS, Boolean.toString(random.nextBoolean()));
										break;
								}

								db.insert(Action._table, null, builder.build(taskId));
							}
						}
					}

					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();

					db.close();
					database.close();

					ContentResolver resolver = getContentResolver();
					resolver.notifyChange(BeaconProvider.buildUri(BeaconProvider.PATH_BEACON), null);
					resolver.notifyChange(BeaconProvider.buildUri(BeaconProvider.PATH_TASK), null);
				}
				return true;

			case android.R.id.home:
				finish();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private static String getName(Random random) {
		return Integer.toString(Math.abs(random.nextInt()), 36).toUpperCase();
	}

	private static String formatTime(int t1, int t2) {
		return String.format(Locale.CANADA, "%04d:%04d", Math.min(t1, t2), Math.max(t1, t2));
	}
}