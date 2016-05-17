package com.coska.beacon.ui.Beacon;

import android.os.Bundle;

import com.coska.beacon.R;
import com.coska.beacon.ui.base.BaseActivity;

/**
 * Created by hwangh on 2016-05-16.
 */
public class EditBeaconActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_pane);
        showFragment(EditBeaconFragment.newInstance(getIntent().getExtras()), R.id.fragment_container, true, false);
        setTitle("New Beacon");
    }
}
