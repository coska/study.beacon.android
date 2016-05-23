package com.coska.beacon;

import android.os.RemoteException;

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

			Identifier namespace = Identifier.parse("0x2f234454f4911ba9ffa6");
			Identifier instanceId = Identifier.parse("0x000000000001");
			Region region = new Region("some_beacon_name", namespace, instanceId, null);

			// looking for a beacon on foreground
			bind(region);

			// looking for a beacon in background
			bootstrap = new RegionBootstrap(this, region);

		} catch (RemoteException e) {
			e.printStackTrace();
			beaconManager.unbind(this);
			bootstrap.disable();
		}
	}

	private void bind() throws RemoteException {
		Region region = new Region("some_unique_value", null, null, null);
		beaconManager.startRangingBeaconsInRegion(region);
		beaconManager.setRangeNotifier(new RangeNotifier() {
			@Override
			public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {

				for (Beacon beacon:collection) {

					// Eddystone-UID frame
					if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) {
						Identifier namespaceId = beacon.getId1();
						Identifier instanceId = beacon.getId2();
						double distanceInMeters = beacon.getDistance();

						if (beacon.getExtraDataFields().size() > 0) {
							long telemetryVersion = beacon.getExtraDataFields().get(0);
							long batteryMilliVolts = beacon.getExtraDataFields().get(1);
							long pduCount = beacon.getExtraDataFields().get(3);
							long uptime = beacon.getExtraDataFields().get(4);
						}
					}
				}
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