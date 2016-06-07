package com.coska.beacon.ui.base;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.coska.beacon.R;

public class BaseFragment extends Fragment {

	protected static volatile int _internal_loader_count = 0;

	@SuppressLint("CommitTransaction")
	protected FragmentTransaction beginTransaction() {
		return getFragmentManager().beginTransaction()
				.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right);
	}
}