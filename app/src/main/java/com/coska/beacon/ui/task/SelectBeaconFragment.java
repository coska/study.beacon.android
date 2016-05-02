package com.coska.beacon.ui.task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coska.beacon.R;
import com.coska.beacon.ui.base.BaseFragment;

public class SelectBeaconFragment extends BaseFragment {
    public static SelectBeaconFragment newInstance(Bundle arguments) {
        SelectBeaconFragment fragment = new SelectBeaconFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extra = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_beacon, container, false);

        initToolbar(rootView);
        initUI(rootView);

        rootView.requestFocus();

        return rootView;
    }

    private void initToolbar(View rootView) {
//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_actionbar);
//        mBaseActivity.setSupportActionBar(toolbar);
//        mBaseActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mBaseActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setTitle(R.string.select_beacon);
    }

    private void initUI(View rootView) {
        rootView.findViewById(R.id.left_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaseActivity.onBackPressed();
            }
        });
        rootView.findViewById(R.id.right_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddTask();
            }
        });
    }

    private void openAddTask() {
        AddTaskFragment fragment = AddTaskFragment.newInstance(new Bundle());
        mBaseActivity.showFragment(fragment, R.id.fragment_container, true, false);
    }
}
