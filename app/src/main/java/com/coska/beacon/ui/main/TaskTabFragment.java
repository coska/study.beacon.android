package com.coska.beacon.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coska.beacon.R;
import com.coska.beacon.ui.base.BaseFragment;
import com.coska.beacon.ui.task.EditTaskActivity;

/**
 * Created by hwangh on 2016-05-09.
 */
public class TaskTabFragment extends BaseFragment {
    public TaskTabFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_task_list, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(View rootView) {
        //noinspection ConstantConditions
        rootView.findViewById(R.id.new_task_floating_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewTask();
            }
        });
    }

    public void updateUi() {

    }

    private void openNewTask() {
        Intent intent = new Intent(getContext(), EditTaskActivity.class);
        startActivity(intent);
    }

}
