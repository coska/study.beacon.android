package com.coska.beacon.ui.task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coska.beacon.R;
import com.coska.beacon.ui.base.BaseFragment;

/**
 * Created by hwangh on 2016-05-16.
 */
public class Step3SetupActionFragment extends BaseFragment {

    public static Step3SetupActionFragment newInstance(Bundle arguments, Fragment target, int requestCode) {
        Step3SetupActionFragment fragment = new Step3SetupActionFragment();
        fragment.setArguments(arguments);
        fragment.setTargetFragment(target, requestCode);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step3_setup_action, container, false);

        initUI(rootView);

        rootView.requestFocus();

        return rootView;
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
                openNextStep();
            }
        });
    }

    private void openNextStep() {
        Step4EditRulesFragment fragment = Step4EditRulesFragment.newInstance(new Bundle(), this, 0);
        mBaseActivity.showFragment(fragment, R.id.fragment_container, true, false);
    }

}
