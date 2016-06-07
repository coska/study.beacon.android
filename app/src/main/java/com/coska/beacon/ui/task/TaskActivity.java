package com.coska.beacon.ui.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.coska.beacon.R;
import com.coska.beacon.ui.base.BaseActivity;
import com.coska.beacon.ui.base.BaseFragment;

public class TaskActivity extends BaseActivity {

	private static final String ID = "_param_id";

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, TaskActivity.class));
	}

	public static void startActivity(Context context, long id) {
		context.startActivity(new Intent(context, TaskActivity.class)
				.putExtra(ID, id));
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_detail_activity);

		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if(savedInstanceState == null) {
			BaseFragment fragment = TaskFragment.getInstance(getIntent().getLongExtra(ID, -1));
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, fragment)
					.commit();
		}
	}
}