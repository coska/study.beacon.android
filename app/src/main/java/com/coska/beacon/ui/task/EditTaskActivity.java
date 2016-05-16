package com.coska.beacon.ui.task;

import android.os.Bundle;

import com.coska.beacon.R;
import com.coska.beacon.ui.base.BaseActivity;

public class EditTaskActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_pane);
        showFragment(Step1SetNameFragment.newInstance(getIntent().getExtras()), R.id.fragment_container, true, false);
        setTitle("New Task");
    }
}
