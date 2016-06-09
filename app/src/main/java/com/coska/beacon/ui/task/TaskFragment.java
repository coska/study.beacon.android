package com.coska.beacon.ui.task;

import android.animation.LayoutTransition;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
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
import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.model.entity.Task;
import com.coska.beacon.model.entity.action.Action;
import com.coska.beacon.model.entity.rule.Rule;
import com.coska.beacon.ui.base.BaseFragment;
import com.coska.beacon.ui.task.action.ActionView;
import com.coska.beacon.ui.task.rule.RuleView;

import org.json.JSONException;

import static com.coska.beacon.model.BeaconProvider.PATH_ACTION;
import static com.coska.beacon.model.BeaconProvider.PATH_BEACON;
import static com.coska.beacon.model.BeaconProvider.PATH_RULE;
import static com.coska.beacon.model.BeaconProvider.PATH_TASK;
import static com.coska.beacon.model.BeaconProvider.buildUri;

public class TaskFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

	private static final String TASK_ID = "_task_id";

	public static BaseFragment getInstance(Long taskId) {

		Bundle bundle = new Bundle(1);
		if(taskId != null) {
			bundle.putLong(TASK_ID, taskId);
		}

		BaseFragment fragment = new TaskFragment();
		fragment.setArguments(bundle);

		return fragment;
	}

	private static final int TASK_LOADER_ID = ++_internal_loader_count;
	private static final int BEACON_LOADER_ID = ++_internal_loader_count;
	private static final int RULE_LOADER_ID = ++_internal_loader_count;
	private static final int ACTION_LOADER_ID = ++_internal_loader_count;

	private static final class Adapter extends SimpleCursorAdapter {
		public Adapter(Context context, Cursor cursor) {
			super(context, android.R.layout.simple_list_item_1, cursor,
					new String[] { Beacon.name }, new int[] { android.R.id.text1 }, FLAG_REGISTER_CONTENT_OBSERVER);
		}
	}

	private EditText name;
	private int loadCount;

	private TextView addRule, addAction;
	private Spinner addBeacon;
	private ViewGroup container;

	public TaskFragment() {
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
				for(int i = 0, count = container.getChildCount(); i < count; i++) {

					View view = container.getChildAt(i);
					if((view instanceof RuleView && !((RuleView) view).validate())
							|| (view instanceof ActionView && !((ActionView) view).validate())) {
						new AlertDialog.Builder(getContext())
								.setCancelable(false)
								.setTitle("Required field")
								.setMessage("Please fill out all the input field")
								.setPositiveButton(android.R.string.ok, null)
								.show();
						return true;
					}
				}

				getActivity().finish();
/*
				ContentValues cv = new ContentValues(1);
				cv.put(Beacon.name, name.getText().toString());

				Bundle bundle = getArguments();
				if(bundle.containsKey(TASK_ID)) {
					getContext().getContentResolver().update(uri, cv, Beacon.uuid + "=?", new String[] { uuid });

				} else {
					getContext().getContentResolver().insert(uri, cv);
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
		layout.setHint("Task Name");
		name = (EditText) layout.findViewById(R.id.name);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.task_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		container = (ViewGroup) view.findViewById(R.id.container);

		addBeacon = (Spinner) container.findViewById(R.id.add_beacon);

		addRule = (TextView) container.findViewById(R.id.add_rule);
		addRule.setOnClickListener(this);
		addRule.setGravity(Gravity.CENTER);

		addAction = (TextView) container.findViewById(R.id.add_action);
		addAction.setOnClickListener(this);
		addAction.setGravity(Gravity.CENTER);

		if(getArguments().containsKey(TASK_ID)) {

			loadCount = 4;
			LoaderManager manager = getLoaderManager();
			manager.initLoader(TASK_LOADER_ID, null, this);
			manager.initLoader(BEACON_LOADER_ID, null, this);
			manager.initLoader(RULE_LOADER_ID, null, this);
			manager.initLoader(ACTION_LOADER_ID, null, this);

		} else {

			loadCount = 4;
			getLoaderManager().initLoader(BEACON_LOADER_ID, null, this);
		}
	}

	protected void inflateRule(int resId) {
		addRule.setGravity(Gravity.RIGHT);
		View view = LayoutInflater.from(getContext()).inflate(resId, container, false);
		container.addView(view, container.indexOfChild(addRule));
	}

	protected void inflateAction(int resId) {
		addAction.setGravity(Gravity.RIGHT);
		View view = LayoutInflater.from(getContext()).inflate(resId, container, false);
		container.addView(view, container.indexOfChild(addAction));
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
			case R.id.add_rule:
				RuleOptionDialog.show(getFragmentManager());
				break;

			case R.id.add_action:
				ActionOptionDialog.show(getFragmentManager());
				break;
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		final long taskId = getArguments().getLong(TASK_ID);
		if(id == TASK_LOADER_ID) {
			return new CursorLoader(getContext(), buildUri(PATH_TASK, taskId), null, null, null, null);

		} else if(id == BEACON_LOADER_ID) {
			return new CursorLoader(getContext(), buildUri(PATH_BEACON), null, null, null, null);

		} else if(id == RULE_LOADER_ID) {
			return new CursorLoader(getContext(), buildUri(PATH_TASK, taskId, PATH_RULE), null, null, null, null);

		} else if(id == ACTION_LOADER_ID) {
			return new CursorLoader(getContext(), buildUri(PATH_TASK, taskId, PATH_ACTION), null, null, null, null);

		} else throw new IllegalArgumentException();
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		final int id = loader.getId();
		if(id == TASK_LOADER_ID) {
			if(cursor.moveToFirst()) {
				name.setText(cursor.getString(cursor.getColumnIndex(Task.name)));

			} else throw new IllegalStateException();

		} else if(id == BEACON_LOADER_ID) {
			addBeacon.setAdapter(new Adapter(getContext(), cursor));

		} else if(id == RULE_LOADER_ID) {

			final int index = container.indexOfChild(addRule);
			LayoutInflater inflater = LayoutInflater.from(getContext());

			final int type = cursor.getColumnIndex(Rule.type);
			final int configuration = cursor.getColumnIndex(Rule.configuration);
			for(int i = 0; cursor.moveToPosition(i); i++) {

				RuleView ruleView = null;
				switch (cursor.getInt(type)) {
					case 0:
						try {
							(ruleView = (RuleView) inflater.inflate(R.layout.task_rule_time, container, false))
									.setConfiguration(cursor.getString(configuration));
						} catch (JSONException ignore) { }
						break;

					case 1:
						try {
							(ruleView = (RuleView) inflater.inflate(R.layout.task_rule_location, container, false))
									.setConfiguration(cursor.getString(configuration));
						} catch (JSONException ignore) { }
						break;
				}

				container.addView(ruleView, index+i);
			}

		} else if(id == ACTION_LOADER_ID) {

			final int index = container.indexOfChild(addAction);
			LayoutInflater inflater = LayoutInflater.from(getContext());

			final int type = cursor.getColumnIndex(Action.type);
			final int configuration = cursor.getColumnIndex(Action.configuration);
			for(int i = 0; cursor.moveToPosition(i); i++) {

				ActionView actionView = null;
				switch (cursor.getInt(type)) {
					case 0:
						try {
							(actionView = (ActionView) inflater.inflate(R.layout.task_action_message, container, false))
									.setConfiguration(cursor.getString(configuration));
						} catch(JSONException ignore) { }
						break;

					case 1:
						try {
							(actionView = (ActionView) inflater.inflate(R.layout.task_action_phone_call, container, false))
									.setConfiguration(cursor.getString(configuration));
						} catch(JSONException ignore) { }
						break;

					case 2:
						try {
							(actionView = (ActionView) inflater.inflate(R.layout.task_action_wifi, container, false))
									.setConfiguration(cursor.getString(configuration));
						} catch(JSONException ignore) { }
						break;
				}

				container.addView(actionView, index+i);
			}
		}

		if(--loadCount == 0) {
			container.setLayoutTransition(new LayoutTransition());
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) { }
}