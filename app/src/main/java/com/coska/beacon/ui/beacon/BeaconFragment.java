package com.coska.beacon.ui.beacon;

import android.bluetooth.BluetoothAdapter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import com.coska.beacon.R;
import com.coska.beacon.model.BeaconProvider;
import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.model.entity.Signal;
import com.coska.beacon.ui.base.BaseFragment;

import static com.coska.beacon.model.BeaconProvider.PATH_BEACON;
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

	private static final int LOADER_ID = ++_internal_loader_count;

	private EditText name;

	private TextView uuid, major, minor;
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
							.setMessage("Please fill out all the input field")
							.setPositiveButton(android.R.string.ok, null)
							.show();
					return true;
				}

				getActivity().finish();
/*
				ContentValues cv = new ContentValues(1);
				cv.put(Beacon.name, name.getText().toString());

				String uuid = getArguments().getString(BEACON_UUID);
				if(TextUtils.isEmpty(uuid)) {
					getContext().getContentResolver().insert(uri, cv);

				} else {
					getContext().getContentResolver().update(uri, cv, Beacon.uuid + "=?", new String[] { uuid });
				}
*/
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

		uuid = (TextView) view.findViewById(R.id.uuid);
		major = (TextView) view.findViewById(R.id.major);
		minor = (TextView) view.findViewById(R.id.minor);

		distance = (TextView) view.findViewById(R.id.distance);
		telemetry = (TextView) view.findViewById(R.id.telemetry);
		battery = (TextView) view.findViewById(R.id.battery);
		pduCount = (TextView) view.findViewById(R.id.pdu_count);
		uptime = (TextView) view.findViewById(R.id.uptime);
		time = (TextView) view.findViewById(R.id.time);

		View fab = view.findViewById(R.id.fab);

		if(getArguments().containsKey(BEACON_ID)) {
			fab.setVisibility(View.GONE);
			getLoaderManager().initLoader(LOADER_ID, null, this);

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

				default:
					adapter.enable();
					break;
			}
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		return new CursorLoader(getContext(), buildUri(PATH_BEACON, getArguments().getLong(BEACON_ID)), null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if(cursor.moveToFirst()) {
			fillInput(cursor);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) { }

	protected void fillInput(Cursor cursor) {

		final int index = cursor.getColumnIndex(Beacon.name);
		if(0 <= index) {
			name.setText(cursor.getString(index));
		}

		uuid.setText(cursor.getString(cursor.getColumnIndex(Beacon.uuid)));
		major.setText(cursor.getString(cursor.getColumnIndex(Beacon.major)));
		minor.setText(cursor.getString(cursor.getColumnIndex(Beacon.minor)));

		distance.setText(cursor.getString(cursor.getColumnIndex(Signal.distance)));
		telemetry.setText(cursor.getString(cursor.getColumnIndex(Signal.telemetry)));
		battery.setText(cursor.getString(cursor.getColumnIndex(Signal.battery)));
		pduCount.setText(cursor.getString(cursor.getColumnIndex(Signal.pduCount)));
		uptime.setText(cursor.getString(cursor.getColumnIndex(Signal.uptime)));
		time.setText(cursor.getString(cursor.getColumnIndex(Signal.time)));
	}
}