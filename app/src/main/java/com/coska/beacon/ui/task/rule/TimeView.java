package com.coska.beacon.ui.task.rule;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentResolver;
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
import com.coska.beacon.model.entity.rule.Rule;
import com.coska.beacon.model.entity.rule.Time;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import static com.coska.beacon.model.BeaconProvider.PATH_RULE;
import static com.coska.beacon.model.BeaconProvider.PATH_TASK;
import static com.coska.beacon.model.BeaconProvider.buildUri;

public class TimeView extends RuleView implements OnClickListener, OnCheckedChangeListener {

	private CheckBox[] cbx = new CheckBox[7];
	private TextView[] from = new TextView[7];
	private TextView[] to = new TextView[7];
	private long id = -1;

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
				((TextView) view).setText(String.format(Locale.CANADA, "%04d", hourOfDay*100 + minute));
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

		for(int i = 0; i < 7; i++) {
			if(cbx[i].isChecked()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void setConfiguration(long id, String configuration) throws JSONException {
		this.id = id;

		JSONObject json = new JSONObject(configuration);
		for(int i = 0; i < Time._WEEK.length; i++) {

			if(json.has(Time._WEEK[i])) {
				cbx[i].setChecked(true);

				String[] array = json.optString(Time._WEEK[i]).split("[^0-9]");
				from[i].setText(array[0]);
				to[i].setText(array[1]);
			}
		}
	}

	@Override
	public int persist(long taskId) {

		Rule.Builder builder = new Rule.Builder()
				.type(Rule.Type.Time);
		for(int i = 0; i < Time._WEEK.length; i++) {
			if(cbx[i].isChecked()) {
				builder.set(Time._WEEK[i], from[i].getText() + ":" + to[i].getText());
			}
		}

		ContentResolver resolver = getContext().getContentResolver();
		if(0 <= id) {
			return resolver.update(buildUri(PATH_RULE, id), builder.build(taskId), null, null);

		} else {
			resolver.insert(buildUri(PATH_TASK, taskId, PATH_RULE), builder.build(taskId));
			return 1;
		}
	}
}