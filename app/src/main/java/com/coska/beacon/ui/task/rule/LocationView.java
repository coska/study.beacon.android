package com.coska.beacon.ui.task.rule;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.AttributeSet;

import com.coska.beacon.model.entity.rule.Rule;

import org.json.JSONException;

import static com.coska.beacon.model.BeaconProvider.PATH_RULE;
import static com.coska.beacon.model.BeaconProvider.PATH_TASK;
import static com.coska.beacon.model.BeaconProvider.buildUri;

public class LocationView extends RuleView {

	private long id = -1;

	public LocationView(Context context) {
		super(context);
	}

	public LocationView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LocationView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public void setConfiguration(long id, String configuration) throws JSONException {
		this.id = id;
	}

	@Override
	public int persist(long taskId) {

		ContentValues cv = new Rule.Builder()
				.type(Rule.Type.Location)
				.build(taskId);

		ContentResolver resolver = getContext().getContentResolver();
		if(0 <= id) {
			return resolver.update(buildUri(PATH_RULE, id), cv, null, null);

		} else {
			resolver.insert(buildUri(PATH_TASK, taskId, PATH_RULE), cv);
			return 1;
		}
	}
}