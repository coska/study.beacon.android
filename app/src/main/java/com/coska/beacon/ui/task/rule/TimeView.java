package com.coska.beacon.ui.task.rule;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
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
import com.coska.beacon.model.entity.rule.Time;

import org.json.JSONException;
import org.json.JSONObject;

public class TimeView extends RuleView implements OnClickListener, OnCheckedChangeListener {

	private CheckBox[] cbx = new CheckBox[7];
	private TextView[] from = new TextView[7];
	private TextView[] to = new TextView[7];
	private JSONObject json = null;

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
		for(int row = 0; row < 7; row++) {

			cbx[row] = (CheckBox) gridLayout.getChildAt(row*3);
			cbx[row].setOnCheckedChangeListener(this);
			cbx[row].setTag(row);

			from[row] = (TextView) gridLayout.getChildAt(row*3+1);
			from[row].setOnClickListener(this);

			to[row] = (TextView) gridLayout.getChildAt(row*3+2);
			to[row].setOnClickListener(this);
		}
	}

	@Override
	public void onClick(final View view) {

		final int time = Integer.parseInt(((TextView) view).getText().toString());
		new TimePickerDialog(getContext(), new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker picker, int hourOfDay, int minute) {
				((TextView) view).setText(String.valueOf(hourOfDay*100 + minute));
			}
		}, time/100, time%100, true).show();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		final int index = (Integer) buttonView.getTag();
		if(isChecked) {
			from[index].setVisibility(View.VISIBLE);
			to[index].setVisibility(View.VISIBLE);

		} else {
			from[index].setVisibility(View.INVISIBLE);
			to[index].setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public void setConfiguration(String configuration) throws JSONException {
		json = new JSONObject(configuration);
		for(int i = 0; i < Time._WEEK.length; i++) {
			init(i, Time._WEEK[i]);
		}
	}

	private void init(int index, String key) {

		if(json.has(key)) {
			cbx[index].setChecked(true);

			String[] array = json.optString(key).split("[^0-9]");
			from[index].setText(array[0]);
			to[index].setText(array[1]);
		}
	}

	@Override
	public String getConfiguration() {
		try {
			if(json != null) {
				for(int i = 0; i < Time._WEEK.length; i++) {
					load(i, Time._WEEK[i]);
				}

				return json.toString();
			}

		} catch (JSONException ignore) { }

		return "";
	}

	private void load(int index, String key) throws JSONException {
		if(cbx[index].isChecked()) {
			json.put(key, from[index].getText() + ":" + to[index].getText());
		}
	}
}