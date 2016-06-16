package com.coska.beacon.ui.task;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.coska.beacon.R;

public class RuleOptionDialog extends AppCompatDialogFragment implements DialogInterface.OnClickListener {

	protected static void show(FragmentManager manager) {
		new RuleOptionDialog().show(manager, RuleOptionDialog.class.getName());
	}

	private static final int layout = android.R.layout.simple_list_item_1;
	private static final String[] rules = new String[] { "Time", "Location" };
	private static final int[] icons = new int[] { R.mipmap.ic_action_time, R.mipmap.ic_action_place };
	private static final String target = TaskFragment.class.getName();

	public RuleOptionDialog() {
		setCancelable(false);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Context context = getContext();
		ListAdapter adapter = new ArrayAdapter<String>(context, layout, rules) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView view = (TextView) super.getView(position, convertView, parent);
				view.setCompoundDrawablePadding(view.getPaddingLeft());
				view.setCompoundDrawablesWithIntrinsicBounds(icons[position], 0, 0, 0);
				return view;
			}
		};

		return new AlertDialog.Builder(context)
				.setTitle("Add Rule")
				.setAdapter(adapter, this)
				.setNegativeButton(android.R.string.cancel, null)
				.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		TaskFragment fragment = (TaskFragment) getFragmentManager().findFragmentByTag(target);
		switch (which) {
			case 0:
				fragment.inflateRule(R.layout.task_rule_time);
				break;

			case 1:
				fragment.inflateRule(R.layout.task_rule_location);
				break;
		}
	}
}