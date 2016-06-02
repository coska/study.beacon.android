package com.coska.beacon;

import android.content.ContentValues;
import android.net.Uri;
import android.os.RemoteException;

import com.coska.beacon.model.BeaconProvider;
import com.coska.beacon.model.entity.Signal;

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
		try {
			bind();  // scanning all beacons around for ids and distance
/*
			Identifier namespace = Identifier.parse("0x2f234454f4911ba9ffa6");
			Identifier instanceId = Identifier.parse("0x000000000001");
			Region region = new Region("some_beacon_name", namespace, instanceId, null);

			// looking for a beacon on foreground
			bind(region);

			// looking for a beacon in background
			bootstrap = new RegionBootstrap(this, region);
*/
		} catch (RemoteException e) {
			e.printStackTrace();
			beaconManager.unbind(this);
			bootstrap.disable();
		}
	}

	private void bind() throws RemoteException {
		beaconManager.startRangingBeaconsInRegion(new Region("some_unique_value", null, null, null));
		beaconManager.setRangeNotifier(new RangeNotifier() {

			private final Uri uri = BeaconProvider.buildUri(BeaconProvider.PATH_SIGNAL);

			@Override
			public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {

				List<ContentValues> list = new ArrayList<>(collection.size());
				for (Beacon beacon:collection) {

					ContentValues cv = new ContentValues();
					cv.put(Signal.uuid, String.valueOf(beacon.getServiceUuid()));
/*
					// Eddystone-UID frame
					if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) { }
*/
					List<Identifier> identifiers = beacon.getIdentifiers();
					switch (identifiers.size()) {
						default:
						case 2: cv.put(Signal.minor, String.valueOf(identifiers.get(1)));
						case 1: cv.put(Signal.major, String.valueOf(identifiers.get(0)));
						case 0:
					}

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

				getContentResolver().bulkInsert(uri, list.toArray(new ContentValues[list.size()]));
			}
		});
	}

	private void bind(Region region) throws RemoteException {
		beaconManager.startMonitoringBeaconsInRegion(region);
		beaconManager.setMonitorNotifier(this);
	}

	@Override
	public void didEnterRegion(Region region) {
		Identifier namespace = region.getId1();
		Identifier instanceId = region.getId2();
	}

	@Override
	public void didExitRegion(Region region) {

	}

	@Override
	public void didDetermineStateForRegion(int state, Region region) {

	}
}