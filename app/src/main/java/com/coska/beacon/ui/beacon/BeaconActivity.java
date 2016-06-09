package com.coska.beacon.ui.beacon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.coska.beacon.R;
import com.coska.beacon.ui.base.BaseActivity;
import com.coska.beacon.ui.base.BaseFragment;

public class BeaconActivity extends BaseActivity {

	private static final String BEACON_ID = "_beacon_id";

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, BeaconActivity.class));
	}

	public static void startActivity(Context context, long id) {
		context.startActivity(new Intent(context, BeaconActivity.class)
				.putExtra(BEACON_ID, id));
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_detail_activity);

		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		BaseFragment fragment = null;
		if(getIntent().hasExtra(BEACON_ID)) {

			((TextView) findViewById(android.R.id.title)).setText("Edit Beacon");
			if(savedInstanceState == null) {
				fragment = BeaconFragment.getInstance(getIntent().getLongExtra(BEACON_ID, -1));
			}

		} else {
			((TextView) findViewById(android.R.id.title)).setText("Add Beacon");
			if(savedInstanceState == null) {
				fragment = BeaconFragment.getInstance(null);
			}
		}

		if(fragment != null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, fragment, BeaconFragment.class.getName())
					.commit();
		}
	}
}