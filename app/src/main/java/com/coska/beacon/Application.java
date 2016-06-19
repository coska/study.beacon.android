package com.coska.beacon;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import com.coska.beacon.model.entity.Signal;
import com.coska.beacon.service.TaskService;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.coska.beacon.model.BeaconProvider.PATH_BEACON;
import static com.coska.beacon.model.BeaconProvider.PATH_SIGNAL;
import static com.coska.beacon.model.BeaconProvider.buildUri;

public class Application extends android.app.Application implements BootstrapNotifier, BeaconConsumer {

	// static string reference for battery saving.
	private BackgroundPowerSaver backgroundPowerSaver;

	private BeaconManager beaconManager;
	private RegionBootstrap bootstrap;

	@Override
	public void onCreate() {
		super.onCreate();

		beaconManager = BeaconManager.getInstanceForApplication(this);
		List<BeaconParser> parsers = beaconManager.getBeaconParsers();
		parsers.add(new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));
		parsers.add(new BeaconParser().setBeaconLayout(BeaconParser.URI_BEACON_LAYOUT));
		parsers.add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
		parsers.add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
		parsers.add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
		beaconManager.bind(this);

		backgroundPowerSaver = new BackgroundPowerSaver(this);
	}

	@Override
	public void onBeaconServiceConnect() {
		Cursor cursor = getContentResolver().query(buildUri(PATH_BEACON), null, null, null, null);
		try {
			final int name = cursor.getColumnIndex(com.coska.beacon.model.entity.Beacon.name);
			final int uuid = cursor.getColumnIndex(com.coska.beacon.model.entity.Beacon.identifier1);
			final int major = cursor.getColumnIndex(com.coska.beacon.model.entity.Beacon.identifier2);
			final int minor = cursor.getColumnIndex(com.coska.beacon.model.entity.Beacon.identifier3);

			for(int i = 0; cursor.moveToPosition(i); i++) {
				startScanning(new Region(cursor.getString(name),
						Identifier.parse(cursor.getString(uuid)),
						Identifier.parse(cursor.getString(major)),
						Identifier.parse(cursor.getString(minor))));
			}
/*
			// looking for a beacon in background
			bootstrap = new RegionBootstrap(this, region);
*/
		} catch (RemoteException e) {
			e.printStackTrace();
			beaconManager.unbind(this);
			bootstrap.disable();

		} finally {
			cursor.close();
		}
	}

	public void startScanning() throws RemoteException {

		beaconManager.startRangingBeaconsInRegion(new Region("scanning", null, null, null));
		beaconManager.setRangeNotifier(new RangeNotifier() {

			private final Uri uri = buildUri(PATH_SIGNAL);

			@Override
			public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {

				List<ContentValues> list = new ArrayList<>(collection.size());
				for (Beacon beacon:collection) {

/*
					// Eddystone-UID frame
					if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) { }
*/
					List<Identifier> identifiers = beacon.getIdentifiers();
					if(1 < identifiers.size()) {

						ContentValues cv = new ContentValues();

						switch (identifiers.size()) {
							default: cv.put(Signal.identifier3, String.valueOf(identifiers.get(2)));
							case 2: cv.put(Signal.identifier2, String.valueOf(identifiers.get(1)));
								cv.put(Signal.identifier1, String.valueOf(identifiers.get(0)));
						}

						cv.put(Signal.name, beacon.getBluetoothName());
						cv.put(Signal.distance, beacon.getDistance());

						List<Long> extra = beacon.getExtraDataFields();
						switch (extra.size()) {
							default:
							case 5: cv.put(Signal.uptime, extra.get(4));
							case 4: cv.put(Signal.pduCount, extra.get(3));
							case 3:
							case 2: cv.put(Signal.battery, extra.get(1));
							case 1: cv.put(Signal.telemetry, extra.get(0));
							case 0:
						}

						list.add(cv);
					}
				}

				getContentResolver().bulkInsert(uri, list.toArray(new ContentValues[list.size()]));
			}
		});
	}

	public void stopScanning() throws RemoteException {
		beaconManager.stopRangingBeaconsInRegion(new Region("scanning", null, null, null));
	}

	public void startScanning(Region region) throws RemoteException {
		beaconManager.startMonitoringBeaconsInRegion(region);
		beaconManager.setMonitorNotifier(this);
	}

	public void stopScanning(Region region) throws RemoteException {
		beaconManager.stopMonitoringBeaconsInRegion(region);
	}

	@Override
	public void didEnterRegion(Region region) {

		ArrayList<String> identifiers = new ArrayList<>(3);
		Identifier namespace = region.getId1();
		if(namespace != null) {
			identifiers.add(namespace.toString());

			Identifier instanceId = region.getId2();
			if(instanceId != null) {
				identifiers.add(instanceId.toString());

				Identifier extra = region.getId3();
				if(extra != null) {
					identifiers.add(extra.toString());
				}
			}
		}

		TaskService.startService(this, identifiers.toArray(new String[identifiers.size()]));
	}

	@Override
	public void didExitRegion(Region region) { }

	@Override
	public void didDetermineStateForRegion(int state, Region region) { }
}