package com.coska.beacon.ui.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.coska.beacon.R;
import com.coska.beacon.ui.base.BaseActivity;
import com.coska.beacon.ui.base.BaseFragment;

public class TaskActivity extends BaseActivity {

	private static final String TASK_ID = "_task_id";
	private static final String BEACON_ID = "_beacon_id";

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, TaskActivity.class));
	}

	public static void startActivity(Context context, long taskId, long beaconId) {
		context.startActivity(new Intent(context, TaskActivity.class)
				.putExtra(TASK_ID, taskId)
				.putExtra(BEACON_ID, beaconId));
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_detail_activity);

		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();

		BaseFragment fragment = null;
		if(intent.hasExtra(TASK_ID)) {

			((TextView) findViewById(android.R.id.title)).setText("Edit Task");
			if(savedInstanceState == null) {
				fragment = TaskFragment.getInstance(intent.getLongExtra(TASK_ID, -1), intent.getLongExtra(BEACON_ID, -1));
			}

		} else {
			((TextView) findViewById(android.R.id.title)).setText("Add Task");
			if(savedInstanceState == null) {
				fragment = TaskFragment.getInstance();
			}
		}

		if(fragment != null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, fragment, TaskFragment.class.getName())
					.commit();
		}
	}
}