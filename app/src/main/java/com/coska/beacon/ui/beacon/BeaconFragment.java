package com.coska.beacon.ui.beacon;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.coska.beacon.Application;
import com.coska.beacon.R;
import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.model.entity.Signal;
import com.coska.beacon.ui.base.BaseFragment;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.coska.beacon.model.BeaconProvider.PATH_BEACON;
import static com.coska.beacon.model.BeaconProvider.PATH_SIGNAL;
import static com.coska.beacon.model.BeaconProvider.buildUri;

public class BeaconFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

	private static final String BEACON_ID = "_beacon_id";

	public static BaseFragment getInstance(Long beaconId) {

		Bundle bundle = new Bundle(1);
		if(beaconId != null) {
			bundle.putLong(BEACON_ID, beaconId);
		}

		BaseFragment fragment = new BeaconFragment();
		fragment.setArguments(bundle);

		return fragment;
	}

	private static final int BEACON_LOADER_ID = ++_internal_loader_count;
	protected static final int SIGNAL_LOADER_ID = ++_internal_loader_count;
	private static final String SIGNAL_WHERE = Signal.identifier1 + "=? AND " + Signal.identifier2 + "=? AND " + Signal.identifier3 + "=?";

	private static final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.CANADA);
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("dd'd 'HH'h 'mm'm 'ss's'", Locale.CANADA);

	private EditText name;

	private TextView identifier1, identifier2, identifier3;
	private TextView distance, telemetry, battery, pduCount, uptime, time;
	private CoordinatorLayout coordinator;

	public BeaconFragment() {
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_accept, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.accept:
				if(TextUtils.isEmpty(name.getText())) {
					new AlertDialog.Builder(getContext())
							.setCancelable(false)
							.setTitle("Required field")
							.setMessage("Please provide task name")
							.setPositiveButton(android.R.string.ok, null)
							.show();
					return true;
				}

				if(TextUtils.isEmpty(identifier1.getText())
						&& TextUtils.isEmpty(identifier2.getText())
						&& TextUtils.isEmpty(identifier3.getText())) {
					new AlertDialog.Builder(getContext())
							.setCancelable(false)
							.setTitle("Required field")
							.setMessage("Please select beacon from searching dialog")
							.setPositiveButton(android.R.string.ok, null)
							.show();
					return true;
				}

				ContentResolver resolver = getContext().getContentResolver();

				ContentValues cv = new ContentValues(1);
				cv.put(Beacon.name, name.getText().toString());

				Bundle bundle = getArguments();
				if(bundle.containsKey(BEACON_ID)) {
					resolver.update(buildUri(PATH_BEACON, bundle.getLong(BEACON_ID)), cv, null, null);

				} else {
					cv.put(Beacon.identifier1, identifier1.getText().toString());
					cv.put(Beacon.identifier2, identifier2.getText().toString());
					cv.put(Beacon.identifier3, identifier3.getText().toString());

					resolver.insert(buildUri(PATH_BEACON), cv);
				}

				getActivity().finish();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		FragmentActivity activity = getActivity();
		TextInputLayout layout = (TextInputLayout) activity.findViewById(R.id.name_wrapper);
		(layout).setHint("Beacon Name");
		name = (EditText) activity.findViewById(R.id.name);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.beacon_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		coordinator = (CoordinatorLayout) view.findViewById(R.id.coordinator);

		identifier1 = (TextView) view.findViewById(R.id.identifier1);
		identifier2 = (TextView) view.findViewById(R.id.identifier2);
		identifier3 = (TextView) view.findViewById(R.id.identifier3);

		distance = (TextView) view.findViewById(R.id.distance);
		telemetry = (TextView) view.findViewById(R.id.telemetry);
		battery = (TextView) view.findViewById(R.id.battery);
		pduCount = (TextView) view.findViewById(R.id.pdu_count);
		uptime = (TextView) view.findViewById(R.id.uptime);
		time = (TextView) view.findViewById(R.id.time);

		View fab = view.findViewById(R.id.fab);

		if(getArguments().containsKey(BEACON_ID)) {
			fab.setVisibility(View.GONE);
			getLoaderManager().initLoader(BEACON_LOADER_ID, null, this);
			try {
				((Application) getContext().getApplicationContext()).startScanning();
			} catch (RemoteException e) {
				e.printStackTrace();
			}

		} else {
			fab.setOnClickListener(this);

			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			if(adapter == null || !adapter.isEnabled()) {
				coordinator.postDelayed(new Runnable() {
					@Override
					public void run() {
						Snackbar.make(coordinator, "Please enable Bluetooth", Snackbar.LENGTH_INDEFINITE)
								.setAction("Enable", BeaconFragment.this)
								.show();
					}
				}, 1000);
			}
		}
	}

	@Override
	public void onClick(View view) {

		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if(adapter != null) {
			switch (view.getId()) {
				case R.id.fab:
					BeaconSearchDialog.show(getFragmentManager());
					try {
						((Application) getContext().getApplicationContext()).startScanning();
					} catch (RemoteException e) {
						e.printStackTrace();
					}

				default:
					adapter.enable();
					break;
			}
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		if(id == BEACON_LOADER_ID) {
			return new CursorLoader(getContext(), buildUri(PATH_BEACON, getArguments().getLong(BEACON_ID)), null, null, null, null);

		} else if(id == SIGNAL_LOADER_ID) {
			return new CursorLoader(getContext(), buildUri(PATH_SIGNAL), null, SIGNAL_WHERE,
					new String[] { bundle.getString(Signal.identifier1, ""), bundle.getString(Signal.identifier2, ""), bundle.getString(Signal.identifier3, "") }, null);

		} else throw new IllegalArgumentException();
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if(cursor.moveToFirst()) {

			final int id = loader.getId();
			if(id == BEACON_LOADER_ID) {
				final int index = cursor.getColumnIndex(Beacon.name);
				if(0 <= index) {
					name.setText(cursor.getString(index));
				}

				identifier1.setText(cursor.getString(cursor.getColumnIndex(Beacon.identifier1)));
				identifier2.setText(cursor.getString(cursor.getColumnIndex(Beacon.identifier2)));
				identifier3.setText(cursor.getString(cursor.getColumnIndex(Beacon.identifier3)));

				Bundle bundle = new Bundle(3);
				bundle.putString(Signal.identifier1, identifier1.getText().toString());
				bundle.putString(Signal.identifier2, identifier2.getText().toString());
				bundle.putString(Signal.identifier3, identifier3.getText().toString());

				getLoaderManager().initLoader(SIGNAL_LOADER_ID, bundle, this);

			} else if(id == SIGNAL_LOADER_ID) {
				identifier1.setText(cursor.getString(cursor.getColumnIndex(Signal.identifier1)));
				identifier2.setText(cursor.getString(cursor.getColumnIndex(Signal.identifier2)));
				identifier3.setText(cursor.getString(cursor.getColumnIndex(Signal.identifier3)));

				distance.setText(String.format(Locale.CANADA, "%.2f m", cursor.getDouble(cursor.getColumnIndex(Signal.distance))));
				telemetry.setText(cursor.getString(cursor.getColumnIndex(Signal.telemetry)));
				battery.setText(cursor.getLong(cursor.getColumnIndex(Signal.battery)) + " mV");
				pduCount.setText(numberFormat.format(cursor.getLong(cursor.getColumnIndex(Signal.pduCount))));
				uptime.setText(timeFormat.format(cursor.getLong(cursor.getColumnIndex(Signal.uptime)) * 100));
				time.setText(cursor.getString(cursor.getColumnIndex(Signal.time)));

			} else throw new IllegalArgumentException();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) { }

	@Override
	public void onDestroy() {
		try {
			((Application) getContext().getApplicationContext()).startScanning();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		super.onDestroy();
	}
}