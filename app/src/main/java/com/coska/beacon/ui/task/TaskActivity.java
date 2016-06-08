package com.coska.beacon.ui.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.coska.beacon.R;
import com.coska.beacon.ui.base.BaseActivity;

public class TaskActivity extends BaseActivity {

	private static final String TASK_ID = "_task_id";

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, TaskActivity.class));
	}

	public static void startActivity(Context context, long id) {
		context.startActivity(new Intent(context, TaskActivity.class)
				.putExtra(TASK_ID, id));
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_detail_activity);

		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if(getIntent().hasExtra(TASK_ID)) {

			((TextView) findViewById(android.R.id.title)).setText("Edit Task");
			if(savedInstanceState == null) {
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.content, TaskFragment.getInstance(getIntent().getLongExtra(TASK_ID, -1)))
						.commit();
			}

		} else {
			((TextView) findViewById(android.R.id.title)).setText("Add Task");
			if(savedInstanceState == null) {
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.content, TaskFragment.getInstance(null))
						.commit();
			}
		}
	}
}