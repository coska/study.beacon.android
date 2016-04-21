package com.coska.beacon.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.coska.beacon.BaseTest;
import com.coska.beacon.BuildConfig;
import com.coska.beacon.model.entity.action.Action;
import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.model.entity.rule.Rule;
import com.coska.beacon.model.entity.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,
		manifest = Config.NONE,
		sdk = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class DatabaseTest extends BaseTest {

	private Database database;

	@Before
	public void setUp() {
		database = new Database(RuntimeEnvironment.application, null);
	}

	@Test
	public void testBeaconInsertFailure() {

		SQLiteDatabase db = database.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(Beacon.name, "name");
		cv.put(Beacon.major, "major");
		cv.put(Beacon.minor, "minor");
		assertTrue(db.insert(Beacon._table, null, cv) < 0);

		db.close();
	}

	@Test
	public void testTaskInsertFailure() {

		SQLiteDatabase db = database.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(Task._beacon_id, -1);
		assertTrue(db.insert(Task._table, null, cv) < 0);

		db.close();
	}

	@Test
	public void testActionInsertFailure() {

		SQLiteDatabase db = database.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(Action._task_id, -1);
		cv.put(Action.type, 1);
		assertTrue(db.insert(Action._table, null, cv) < 0);

		db.close();
	}

	@Test
	public void testRuleInsertFailure() {

		SQLiteDatabase db = database.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(Rule._task_id, -1);
		cv.put(Rule.type, 1);
		assertTrue(db.insert(Rule._table, null, cv) < 0);

		db.close();
	}

	@Test
	public void testDeleteCascade() {

		SQLiteDatabase db = database.getWritableDatabase();

		ContentValues cv = new ContentValues();
		String uuid = getRandomString();
		cv.put(Beacon.uuid, uuid);
		final long beaconId = db.insert(Beacon._table, null, cv);
		assertTrue(0 <= beaconId);
		cv.clear();

		cv.put(Task._beacon_id, beaconId);
		final long taskId = db.insert(Task._table, null, cv);
		assertTrue(0 <= taskId);
		cv.clear();

		cv.put(Action._task_id, taskId);
		cv.put(Action.type, 1);
		assertTrue(0 <= db.insert(Action._table, null, cv));
		cv.clear();

		cv.put(Rule._task_id, taskId);
		cv.put(Rule.type, 1);
		assertTrue(0 <= db.insert(Rule._table, null, cv));
		cv.clear();

		assertTrue(0 < db.delete(Beacon._table, Beacon.uuid + "=?", new String[] { uuid }));

		Cursor cursor = db.query(Task._table, null, null, null, null, null, null);
		assertEquals(0, cursor.getCount());
		cursor.close();

		cursor = db.query(Action._table, null, null, null, null, null, null);
		assertEquals(0, cursor.getCount());
		cursor.close();

		cursor = db.query(Rule._table, null, null, null, null, null, null);
		assertEquals(0, cursor.getCount());
		cursor.close();

		db.close();
	}

	@After
	public void tearDown() {
		database.close();
	}
}