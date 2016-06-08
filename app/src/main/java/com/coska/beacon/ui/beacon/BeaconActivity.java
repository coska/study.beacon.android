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

	private static final String BEACON_UUID = "_beacon_uuid";

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, BeaconActivity.class));
	}

	public static void startActivity(Context context, String uuid) {
		context.startActivity(new Intent(context, BeaconActivity.class)
			.putExtra(BEACON_UUID, uuid));
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_detail_activity);

		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		String uuid = getIntent().getStringExtra(BEACON_UUID);
		if(uuid == null) {
			((TextView) findViewById(android.R.id.title)).setText("Add Beacon");

		} else {
			((TextView) findViewById(android.R.id.title)).setText("Edit Beacon");
		}

		if(savedInstanceState == null) {
			BaseFragment fragment = BeaconFragment.getInstance(uuid);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, fragment, BeaconFragment.class.getName())
					.commit();
		}
	}
}