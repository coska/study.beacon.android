package com.coska.beacon.ui.test;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.coska.beacon.ui.base.BaseActivity;

public class BeaconsActivity extends BaseActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.replace(android.R.id.content, new BeaconsFragment())
					.commit();
		}
	}
}