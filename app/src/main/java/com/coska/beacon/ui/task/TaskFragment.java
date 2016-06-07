package com.coska.beacon.ui.task;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.coska.beacon.R;
import com.coska.beacon.model.BeaconCursorLoader;
import com.coska.beacon.model.BeaconProvider;
import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.ui.base.BaseFragment;

public class TaskFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

	private static final String ID = "_bundle_id";

	public static BaseFragment getInstance(long id) {

		Bundle bundle = new Bundle(1);
		bundle.putLong(ID, id);

		BaseFragment fragment = new TaskFragment();
		fragment.setArguments(bundle);

		return fragment;
	}

	private static final int LOADER_ID = ++_internal_loader_count;
	private static final Uri uri = BeaconProvider.buildUri(BeaconProvider.PATH_BEACON);

	private static final class Adapter extends SimpleCursorAdapter {
		public Adapter(Context context, Cursor cursor) {
			super(context, android.R.layout.simple_list_item_1, cursor,
					new String[] { Beacon.name }, new int[] { android.R.id.text1 }, FLAG_REGISTER_CONTENT_OBSERVER);
		}
	}

	private EditText name;

	private TextView addRule, addAction;
	private Spinner addBeacon;
	private ViewGroup container;

	public TaskFragment() {
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_add, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.add:
				getFragmentManager().popBackStack();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.task_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		name = (EditText) view.findViewById(R.id.name);
		container = (ViewGroup) view.findViewById(R.id.container);

		addBeacon = (Spinner) view.findViewById(R.id.add_beacon);

		addRule = (TextView) view.findViewById(R.id.add_rule);
		addRule.setOnClickListener(this);
		addRule.setGravity(Gravity.CENTER);

		addAction = (TextView) view.findViewById(R.id.add_action);
		addAction.setOnClickListener(this);
		addAction.setGravity(Gravity.CENTER);

		getLoaderManager().initLoader(LOADER_ID, null, this);
	}

	protected void inflateRule(int resId, int count) {

		addRule.setGravity(Gravity.RIGHT);
		addRule.setTag(count+1);

		View view = LayoutInflater.from(getContext()).inflate(resId, container, false);
		container.addView(view, count+5);
	}

	protected void inflateAction(int resId, int count) {

		addAction.setGravity(Gravity.RIGHT);
		addAction.setTag(count+1);

		View view = LayoutInflater.from(getContext()).inflate(resId, container, false);

		Integer index = (Integer) addRule.getTag();
		container.addView(view, count+7 + (index == null ? 0 : index));
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
			case R.id.add_rule:
				RuleOptionDialog.getInstance((Integer) view.getTag())
						.show(getFragmentManager(), RuleOptionDialog.class.getName());
				break;

			case R.id.add_action:
				ActionOptionDialog.getInstance((Integer) view.getTag())
						.show(getFragmentManager(), ActionOptionDialog.class.getName());
				break;
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//		return new CursorLoader(getContext(), uri, null, null, null, null);
		return new BeaconCursorLoader(getContext());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		addBeacon.setAdapter(new Adapter(getContext(), cursor));
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) { }
}