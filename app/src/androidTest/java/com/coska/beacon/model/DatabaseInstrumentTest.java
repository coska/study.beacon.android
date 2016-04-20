package com.coska.beacon.model;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;

import org.junit.Before;
import org.junit.runner.RunWith;

import static com.coska.beacon.model.BeaconProvider.AUTHORITY;

@RunWith(AndroidJUnit4.class)
public class DatabaseInstrumentTest extends ProviderTestCase2<BeaconProvider> {

	public DatabaseInstrumentTest() {
		super(BeaconProvider.class, AUTHORITY);
	}

	@Before
	@Override
	public void setUp() throws Exception {
		setContext(InstrumentationRegistry.getTargetContext());
		super.setUp();
	}

}