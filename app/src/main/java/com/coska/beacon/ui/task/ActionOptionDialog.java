package com.coska.beacon.ui.task;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.ArrayAdapter;

import com.coska.beacon.R;

public class ActionOptionDialog extends AppCompatDialogFragment implements DialogInterface.OnClickListener {

	private static final String BUNDLE_COUNT = "bundle_count";

	protected static AppCompatDialogFragment getInstance(Integer count) {

		Bundle bundle = new Bundle(1);
		bundle.putInt(BUNDLE_COUNT, count == null ? 0 : count);

		AppCompatDialogFragment fragment = new ActionOptionDialog();
		fragment.setArguments(bundle);

		return fragment;
	}

	private static final int layout = android.R.layout.simple_list_item_1;
	private static final String[] actions = new String[] { "Phone Call", "Message", "Wifi Switch" };
	private static final String target = TaskFragment.class.getName();

	public ActionOptionDialog() {
		setCancelable(false);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Context context = getContext();
		return new AlertDialog.Builder(context)
				.setTitle("Add Action")
				.setAdapter(new ArrayAdapter<>(context, layout, actions), this)
				.setNegativeButton(android.R.string.cancel, null)
				.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		TaskFragment fragment = (TaskFragment) getFragmentManager().findFragmentByTag(target);
		final int count = getArguments().getInt(BUNDLE_COUNT);

		switch (which) {
			case 0:
				fragment.inflateAction(R.layout.task_action_phone_call, count);
				break;

			case 1:
				fragment.inflateAction(R.layout.task_action_message, count);
				break;

			case 2:
				fragment.inflateAction(R.layout.task_action_wifi, count);
				break;
		}
	}
}