package com.coska.beacon.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;

import com.coska.beacon.BaseTest;
import com.coska.beacon.BuildConfig;
import com.coska.beacon.model.entity.action.Action;
import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.model.entity.rule.Rule;
import com.coska.beacon.model.entity.Task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowContentResolver;

import static android.content.ContentUris.parseId;
import static com.coska.beacon.model.BeaconProvider.AUTHORITY;
import static com.coska.beacon.model.BeaconProvider.PATH_ACTION;
import static com.coska.beacon.model.BeaconProvider.PATH_BEACON;
import static com.coska.beacon.model.BeaconProvider.PATH_RULE;
import static com.coska.beacon.model.BeaconProvider.PATH_TASK;
import static com.coska.beacon.model.BeaconProvider.buildUri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
		manifest = Config.NONE,
		sdk = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BeaconProviderTest extends BaseTest {

	private ContentResolver resolver;

	@Before
	public void setUp() throws Exception {
		resolver = RuntimeEnvironment.application.getContentResolver();

		BeaconProvider provider = new BeaconProvider();
		provider.onCreate();
		ShadowContentResolver.registerProvider(AUTHORITY, provider);
	}

	@Test
	public void testBeaconOperation() {

		ContentValues cv = new ContentValues();
		cv.put(Beacon.uuid, "uuid");
		cv.put(Beacon.name, "name");
		cv.put(Beacon.major, "major");
		cv.put(Beacon.minor, "minor");
		cv.put(Beacon.status, "status");

		long beaconId = parseId(resolver.insert(buildUri(PATH_BEACON), cv));
		assertTrue(0 <= beaconId);

		Cursor cursor = resolver.query(buildUri(PATH_BEACON), null, null, null, null);
		assertTrue(cursor != null && cursor.moveToFirst());
		assertEquals(1, cursor.getCount());
		assertEqualsCursor(beaconId, cv, cursor);
		cursor.close();

		cursor = resolver.query(buildUri(PATH_BEACON, beaconId), null, null, null, null);
		assertTrue(cursor != null && cursor.moveToFirst());
		assertEquals(1, cursor.getCount());
		assertEqualsCursor(beaconId, cv, cursor);
		cursor.close();

		cv.clear();
		cv.put(Beacon.name, "new name");
		cv.put(Beacon.major, "new major");
		cv.put(Beacon.minor, "new minor");
		cv.put(Beacon.status, "new status");

		assertEquals(1, resolver.update(buildUri(PATH_BEACON, beaconId), cv, null, null));

		cursor = resolver.query(buildUri(PATH_BEACON), null, null, null, null);
		assertTrue(cursor != null && cursor.moveToFirst());
		assertEquals(1, cursor.getCount());
		assertEqualsCursor(beaconId, cv, cursor);
		assertEquals("uuid", cursor.getString(cursor.getColumnIndex(Beacon.uuid)));
		cursor.close();

		assertEquals(1, resolver.delete(buildUri(PATH_BEACON, beaconId), null, null));

		cursor = resolver.query(buildUri(PATH_BEACON), null, null, null, null);
		assertTrue(cursor != null && !cursor.moveToFirst());
		cursor.close();
	}

	@Test
	public void testTaskOperation() {

		ContentValues cv = new ContentValues();
		cv.put(Beacon.uuid, "uuid");
		cv.put(Beacon.name, "name");
		cv.put(Beacon.major, "major");
		cv.put(Beacon.minor, "minor");
		cv.put(Beacon.status, "status");

		long beaconId = parseId(resolver.insert(buildUri(PATH_BEACON), cv));

		cv.clear();
		cv.put(Task._beacon_id, beaconId);
		cv.put(Task.name, "name");

		long taskId = parseId(resolver.insert(buildUri(PATH_BEACON, beaconId, PATH_TASK), cv));

		Cursor cursor = resolver.query(buildUri(PATH_TASK), null, null, null, null);
		assertTrue(cursor != null && cursor.moveToFirst());
		assertEquals(1, cursor.getCount());
		assertEqualsCursor(taskId, cv, cursor);
		cursor.close();

		cursor = resolver.query(buildUri(PATH_BEACON, beaconId, PATH_TASK), null, null, null, null);
		assertTrue(cursor != null && cursor.moveToFirst());
		assertEquals(1, cursor.getCount());
		assertEqualsCursor(taskId, cv, cursor);
		cursor.close();

		cursor = resolver.query(buildUri(PATH_TASK, taskId), null, null, null, null);
		assertTrue(cursor != null && cursor.moveToFirst());
		assertEquals(1, cursor.getCount());
		assertEqualsCursor(taskId, cv, cursor);
		cursor.close();

		cv.clear();
		cv.put(Task.name, "new name");

		assertEquals(1, resolver.update(buildUri(PATH_TASK, taskId), cv, null, null));

		cursor = resolver.query(buildUri(PATH_TASK, taskId), null, null, null, null);
		assertTrue(cursor != null && cursor.moveToFirst());
		assertEquals(1, cursor.getCount());
		assertEquals(beaconId, cursor.getLong(cursor.getColumnIndex(Task._beacon_id)));
		assertEqualsCursor(taskId, cv, cursor);
		cursor.close();

		assertEquals(1, resolver.delete(buildUri(PATH_TASK, taskId), null, null));

		cursor = resolver.query(buildUri(PATH_TASK), null, null, null, null);
		assertTrue(cursor != null && !cursor.moveToFirst());
		cursor.close();

		assertEquals(1, resolver.delete(buildUri(PATH_BEACON, beaconId), null, null));
	}

	@Test
	public void testRuleOperation() {

		ContentValues cv = new ContentValues();
		cv.put(Beacon.uuid, "uuid");
		cv.put(Beacon.name, "name");
		cv.put(Beacon.major, "major");
		cv.put(Beacon.minor, "minor");
		cv.put(Beacon.status, "status");

		long beaconId = parseId(resolver.insert(buildUri(PATH_BEACON), cv));

		cv.clear();
		cv.put(Task._beacon_id, beaconId);
		cv.put(Task.name, "name");

		long taskId = parseId(resolver.insert(buildUri(PATH_BEACON, beaconId, PATH_TASK), cv));

		cv.clear();
		cv.put(Rule._task_id, taskId);
		cv.put(Rule.name, "name");
		cv.put(Rule.type, 1);
		cv.put(Rule.configuration, "configuration");

		long ruleId = parseId(resolver.insert(buildUri(PATH_TASK, taskId, PATH_RULE), cv));

		Cursor cursor = resolver.query(buildUri(PATH_TASK, taskId, PATH_RULE), null, null, null, null);
		assertTrue(cursor != null && cursor.moveToFirst());
		assertEquals(1, cursor.getCount());
		assertEqualsCursor(ruleId, cv, cursor);
		cursor.close();

		cursor = resolver.query(buildUri(PATH_RULE, ruleId), null, null, null, null);
		assertTrue(cursor != null && cursor.moveToFirst());
		assertEquals(1, cursor.getCount());
		assertEqualsCursor(ruleId, cv, cursor);
		cursor.close();

		cv.clear();
		cv.put(Rule.name, "new name");
		cv.put(Rule.type, 2);
		cv.put(Rule.configuration, "new configuration");

		assertEquals(1, resolver.update(buildUri(PATH_RULE, ruleId), cv, null, null));

		cursor = resolver.query(buildUri(PATH_TASK, taskId, PATH_RULE), null, null, null, null);
		assertTrue(cursor != null && cursor.moveToFirst());
		assertEquals(1, cursor.getCount());
		assertEquals(taskId, cursor.getLong(cursor.getColumnIndex(Rule._task_id)));
		assertEqualsCursor(ruleId, cv, cursor);
		cursor.close();

		assertEquals(1, resolver.delete(buildUri(PATH_RULE, ruleId), null, null));

		cursor = resolver.query(buildUri(PATH_TASK, taskId, PATH_RULE), null, null, null, null);
		assertTrue(cursor != null && !cursor.moveToFirst());
		cursor.close();

		assertEquals(1, resolver.delete(buildUri(PATH_BEACON, beaconId), null, null));
	}

	@Test
	public void testActionOperation() {

		ContentValues cv = new ContentValues();
		cv.put(Beacon.uuid, "uuid");
		cv.put(Beacon.name, "name");
		cv.put(Beacon.major, "major");
		cv.put(Beacon.minor, "minor");
		cv.put(Beacon.status, "status");

		long beaconId = parseId(resolver.insert(buildUri(PATH_BEACON), cv));

		cv.clear();
		cv.put(Task._beacon_id, beaconId);
		cv.put(Task.name, "name");

		long taskId = parseId(resolver.insert(buildUri(PATH_BEACON, beaconId, PATH_TASK), cv));

		cv.clear();
		cv.put(Action._task_id, taskId);
		cv.put(Action.name, "name");
		cv.put(Action.type, 1);
		cv.put(Action.configuration, "configuration");

		long actionId = parseId(resolver.insert(buildUri(PATH_TASK, taskId, PATH_ACTION), cv));

		Cursor cursor = resolver.query(buildUri(PATH_TASK, taskId, PATH_ACTION), null, null, null, null);
		assertTrue(cursor != null && cursor.moveToFirst());
		assertEquals(1, cursor.getCount());
		assertEqualsCursor(actionId, cv, cursor);
		cursor.close();

		cursor = resolver.query(buildUri(PATH_ACTION, actionId), null, null, null, null);
		assertTrue(cursor != null && cursor.moveToFirst());
		assertEquals(1, cursor.getCount());
		assertEqualsCursor(actionId, cv, cursor);
		cursor.close();

		cv.clear();
		cv.put(Rule.name, "new name");
		cv.put(Rule.type, 2);
		cv.put(Rule.configuration, "new configuration");

		assertEquals(1, resolver.update(buildUri(PATH_ACTION, actionId), cv, null, null));

		cursor = resolver.query(buildUri(PATH_TASK, taskId, PATH_ACTION), null, null, null, null);
		assertTrue(cursor != null && cursor.moveToFirst());
		assertEquals(1, cursor.getCount());
		assertEquals(taskId, cursor.getLong(cursor.getColumnIndex(Action._task_id)));
		assertEqualsCursor(actionId, cv, cursor);
		cursor.close();

		assertEquals(1, resolver.delete(buildUri(PATH_ACTION, actionId), null, null));

		cursor = resolver.query(buildUri(PATH_TASK, taskId, PATH_ACTION), null, null, null, null);
		assertTrue(cursor != null && !cursor.moveToFirst());
		cursor.close();

		assertEquals(1, resolver.delete(buildUri(PATH_BEACON, beaconId), null, null));
	}
}