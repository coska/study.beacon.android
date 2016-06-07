package com.coska.beacon.ui.beacon;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.coska.beacon.model.BeaconCursorLoader;
import com.coska.beacon.model.BeaconProvider;
import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.model.entity.Signal;
import com.coska.beacon.ui.base.BaseFragment;

public class BeaconFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final String UUID = "_uuid";

	public static BaseFragment getInstance(String uuid) {
		Bundle bundle = new Bundle(1);
		bundle.putString(UUID, uuid);

		BaseFragment fragment = new BeaconFragment();
		fragment.setArguments(bundle);

		return fragment;
	}

	private static final class BeaconLoader extends CursorLoader {
		public BeaconLoader(Context context, String uuid) {
			super(context, uri, null, Beacon.uuid + "=?", new String[] { uuid }, null);
		}
	}

	private static final int LOADER_ID = ++_internal_loader_count;
	private static final Uri uri = BeaconProvider.buildUri(BeaconProvider.PATH_BEACON);

	private EditText name;
	private TextView uuid, major, minor, battery;

	public BeaconFragment() {
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		Bundle bundle = getArguments();
		if(bundle != null && !TextUtils.isEmpty(bundle.getString(UUID))) {
			inflater.inflate(R.menu.menu_cancel, menu);

		} else {
			inflater.inflate(R.menu.menu_search, menu);
		}

		inflater.inflate(R.menu.menu_accept, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		ContentResolver resolver = getContext().getContentResolver();

		switch (item.getItemId()) {
			case R.id.accept:
				ContentValues cv = new ContentValues(1);
				cv.put(Beacon.name, name.getText().toString());

				Bundle bundle = getArguments();
				if(bundle != null && !TextUtils.isEmpty(bundle.getString(UUID))) {
					resolver.update(uri, cv, Beacon.uuid + "=?", new String[] { bundle.getString(UUID) });

				} else {
					resolver.insert(uri, cv);
				}
				return true;

			case R.id.cancel:
				resolver.delete(uri, Beacon.uuid + "=?", new String[] { getArguments().getString(UUID) });
				return true;

			case R.id.search:
				new BeaconSearchDialog()
						.show(getFragmentManager(), BeaconSearchDialog.class.getName());
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.beacon_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		name = (EditText) view.findViewById(R.id.name);

		uuid = (TextView) view.findViewById(R.id.uuid);
		major = (TextView) view.findViewById(R.id.major);
		minor = (TextView) view.findViewById(R.id.minor);
		battery = (TextView) view.findViewById(R.id.battery);

		Bundle bundle = getArguments();
		if(bundle != null && !TextUtils.isEmpty(bundle.getString(UUID))) {
			getLoaderManager().initLoader(LOADER_ID, new Bundle(bundle), this);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
//		return new BeaconLoader(getContext(), bundle.getString(UUID));
		return new BeaconCursorLoader(getContext());
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
		battery.setText(cursor.getString(cursor.getColumnIndex(Signal.battery)));
	}
}