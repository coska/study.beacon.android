package com.coska.beacon.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.coska.beacon.R;
import com.coska.beacon.ui.base.BaseActivity;
import com.coska.beacon.ui.beacon.BeaconActivity;
import com.coska.beacon.ui.task.TaskActivity;

public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {

	private ViewPager viewPager;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_activity);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		tabLayout.setOnTabSelectedListener(this);

		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(new Adapter(getSupportFragmentManager()));
		viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

		findViewById(R.id.fab).setOnClickListener(this);
	}

	@Override
	public void onTabSelected(TabLayout.Tab tab) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(TabLayout.Tab tab) { }

	@Override
	public void onTabReselected(TabLayout.Tab tab) { }

	@Override
	public void onClick(View view) {

		switch (viewPager.getCurrentItem()) {
			case 0:
				BeaconActivity.startActivity(this);
				break;

			case 1:
				TaskActivity.startActivity(this);
				break;
		}
	}

	public static final class Adapter extends FragmentStatePagerAdapter {

		public Adapter(FragmentManager manager) {
			super(manager);
		}

		@Override
		public Fragment getItem(int position) {

			switch (position) {
				case 0:
					return new BeaconsFragment();
				case 1:
					return new TasksFragment();

				default:
					throw new IndexOutOfBoundsException();
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	}
}