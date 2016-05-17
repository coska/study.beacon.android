package com.coska.beacon.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    protected BaseActivity mBaseActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBaseActivity = (BaseActivity)getActivity();
    }
}