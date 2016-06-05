package com.coska.beacon.ui.task.rule;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

import com.coska.beacon.R;

public class TimeView extends RuleView implements OnClickListener, OnCheckedChangeListener {

	public TimeView(Context context) {
		super(context);
	}

	public TimeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		GridLayout gridLayout = (GridLayout) findViewById(R.id.grid);
		for(int i = 0, count = gridLayout.getChildCount(); i < count; i+=3) {
			CheckBox cbx = (CheckBox) gridLayout.getChildAt(i);
			View from = gridLayout.getChildAt(i+1);
			View to = gridLayout.getChildAt(i+2);

			cbx.setOnCheckedChangeListener(this);
			from.setOnClickListener(this);
			to.setOnClickListener(this);

			cbx.setTag(Pair.create(from, to));
		}
	}

	@Override
	public void onClick(final View view) {

		TimePickerDialog dialog = new TimePickerDialog(getContext(), new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker picker, int hourOfDay, int minute) {
				((TextView) view).setText(hourOfDay + ":" + minute);
			}
		}, 12, 0, true);
		dialog.show();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		Pair<View, View> pair = (Pair<View, View>) buttonView.getTag();
		if(isChecked) {
			pair.first.setVisibility(View.VISIBLE);
			pair.second.setVisibility(View.VISIBLE);

		} else {
			pair.first.setVisibility(View.INVISIBLE);
			pair.second.setVisibility(View.INVISIBLE);
		}
	}
}