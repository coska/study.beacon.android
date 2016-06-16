package com.coska.beacon.ui.beacon;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coska.beacon.R;
import com.coska.beacon.model.entity.Signal;

import static com.coska.beacon.model.BeaconProvider.PATH_SIGNAL;
import static com.coska.beacon.model.BeaconProvider.buildUri;


public class BeaconSearchDialog extends AppCompatDialogFragment implements LoaderManager.LoaderCallbacks<Cursor>, DialogInterface.OnClickListener {

	public static void show(FragmentManager manager) {
		new BeaconSearchDialog().show(manager, BeaconSearchDialog.class.getName());
	}

	private CursorAdapter adapter;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		adapter = new SimpleCursorAdapter(context,
				android.R.layout.simple_list_item_2, null,
				new String[] { Signal.identifier1, Signal.identifier2 },
				new int[] { android.R.id.text1, android.R.id.text2 },
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		getDialog().setTitle("Searching Beacons");
		return inflater.inflate(R.layout.base_list_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getLoaderManager().initLoader(hashCode(), null, this);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		getLoaderManager().initLoader(hashCode(), null, this);
		return new AlertDialog.Builder(getContext())
				.setTitle("Searching Beacons")
				.setAdapter(adapter, this)
				.setNegativeButton("dismiss", null)
				.create();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getContext(), buildUri(PATH_SIGNAL), null, null, null, Signal.identifier1);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) { }

	@Override
	public void onClick(DialogInterface dialog, int which) {

		Cursor cursor = adapter.getCursor();
		if(cursor.moveToPosition(which)) {

			Bundle bundle = new Bundle(3);
			bundle.putString(Signal.identifier1, cursor.getString(cursor.getColumnIndex(Signal.identifier1)));
			bundle.putString(Signal.identifier2, cursor.getString(cursor.getColumnIndex(Signal.identifier2)));
			bundle.putString(Signal.identifier3, cursor.getString(cursor.getColumnIndex(Signal.identifier3)));

			BeaconFragment fragment = (BeaconFragment) getFragmentManager().findFragmentByTag(BeaconFragment.class.getName());
			fragment.getLoaderManager().initLoader(BeaconFragment.SIGNAL_LOADER_ID, bundle, fragment);
		}

		dismiss();
	}
}