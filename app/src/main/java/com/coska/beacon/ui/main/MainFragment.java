package com.coska.beacon.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coska.beacon.R;
import com.coska.beacon.ui.base.BaseFragment;

/**
 * Created by hwangh on 2016-05-09.
 */
public class MainFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(View rootView) {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.container);
        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(1);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state) {}


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        BeaconTabFragment mBeaconTab;
        TaskTabFragment mTaskTab;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new BeaconTabFragment();
                case 1:
                    return new TaskTabFragment();
            }
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);

            switch (position) {
                case 0:
                    mBeaconTab = (BeaconTabFragment)createdFragment;
                    //mBeaconTab.updateUi();
                    break;
                case 1:
                    mTaskTab = (TaskTabFragment) createdFragment;
                    //mTaskTab.updateUi();
                    break;
            }
            return createdFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_beacon);
                case 1:
                    return getString(R.string.tab_Task);
            }
            return null;
        }
    }
}
