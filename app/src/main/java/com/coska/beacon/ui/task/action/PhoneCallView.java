package com.coska.beacon.ui.task.action;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import com.coska.beacon.R;
import com.coska.beacon.model.entity.action.Action;
import com.coska.beacon.model.entity.action.PhoneCall;

import org.json.JSONException;
import org.json.JSONObject;

import static com.coska.beacon.model.BeaconProvider.PATH_ACTION;
import static com.coska.beacon.model.BeaconProvider.PATH_TASK;
import static com.coska.beacon.model.BeaconProvider.buildUri;

public class PhoneCallView extends ActionView {

	private EditText name, number;
	private long id = -1;

	public PhoneCallView(Context context) {
		super(context);
	}

	public PhoneCallView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PhoneCallView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		name = (EditText) findViewById(R.id.name);
		number = (EditText) findViewById(R.id.number);
	}

	@Override
	public boolean validate() {
		return !TextUtils.isEmpty(name.getText())
				&& !TextUtils.isEmpty(number.getText());
	}

	@Override
	public void setConfiguration(long id, String configuration) throws JSONException {
		this.id = id;

		JSONObject json = new JSONObject(configuration);
		name.setText(json.optString(PhoneCall.NAME));
		number.setText(json.optString(PhoneCall.DIAL_NUMBER));
	}

	@Override
	public int persist(long taskId) {
		ContentValues cv = new Action.Builder()
				.type(Action.Type.PHONE_CALL)
				.set(PhoneCall.NAME, name.getText().toString())
				.set(PhoneCall.DIAL_NUMBER, number.getText().toString())
				.build(taskId);

		ContentResolver resolver = getContext().getContentResolver();
		if(0 <= id) {
			return resolver.update(buildUri(PATH_ACTION, id), cv, null, null);

		} else {
			resolver.insert(buildUri(PATH_TASK, taskId, PATH_ACTION), cv);
			return 1;
		}
	}
}