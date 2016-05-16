package com.coska.beacon.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coska.beacon.R;
import com.coska.beacon.ui.Beacon.EditBeaconActivity;
import com.coska.beacon.ui.base.BaseFragment;

/**
 * Created by hwangh on 2016-05-09.
 */
public class BeaconTabFragment extends BaseFragment {
    public BeaconTabFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_beacon_list, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(View rootView) {
        rootView.findViewById(R.id.new_beacon_floating_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewBeacon();
            }
        });
    }

    public void updateUi() {

    }

    private void openNewBeacon() {
        Intent intent = new Intent(getContext(), EditBeaconActivity.class);
        startActivity(intent);
    }

}
