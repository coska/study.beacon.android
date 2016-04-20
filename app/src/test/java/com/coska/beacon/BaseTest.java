package com.coska.beacon;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class BaseTest {

	private Random random = new Random();

	protected String getRandomString() {
		return Long.toString(Math.abs(random.nextLong()), 36);
	}

	protected int getRandomInt(int from, int to) {
		return from + random.nextInt(to - from);
	}

	protected void assertEqualsCursor(long id, ContentValues cv, Cursor cursor) {
		assertEquals(id, cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)));
		assertEqualsCursor(cv, cursor);
	}

	protected void assertEqualsCursor(ContentValues cv, Cursor cursor) {
		for(String key:cv.keySet()) {
			assertEquals(cv.getAsString(key), cursor.getString(cursor.getColumnIndex(key)));
		}
	}
}