package com.coska.beacon.ui.base;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.coska.beacon.R;

public class BaseActivity extends AppCompatActivity {

	@SuppressLint("CommitTransaction")
	protected FragmentTransaction beginTransaction() {
		return getSupportFragmentManager().beginTransaction()
				.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right);
	}
}