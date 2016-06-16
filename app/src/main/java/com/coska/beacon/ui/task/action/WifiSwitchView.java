package com.coska.beacon.ui.task.action;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Switch;

import com.coska.beacon.R;
import com.coska.beacon.model.entity.action.Action;
import com.coska.beacon.model.entity.action.Wifi;

import org.json.JSONException;
import org.json.JSONObject;

import static com.coska.beacon.model.BeaconProvider.PATH_ACTION;
import static com.coska.beacon.model.BeaconProvider.PATH_TASK;
import static com.coska.beacon.model.BeaconProvider.buildUri;

public class WifiSwitchView extends ActionView {

	private Switch wifiSwitch;
	private long id = -1;

	public WifiSwitchView(Context context) {
		super(context);
	}

	public WifiSwitchView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WifiSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		wifiSwitch = (Switch) findViewById(R.id.wifi_switch);
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public void setConfiguration(long id, String configuration) throws JSONException {
		this.id = id;

		JSONObject json = new JSONObject(configuration);
		wifiSwitch.setChecked(json.optBoolean(Wifi.WIFI_STATUS));
	}

	@Override
	public int persist(long taskId) {
		ContentValues cv = new Action.Builder()
				.type(Action.Type.WIFI)
				.set(Wifi.WIFI_STATUS, Boolean.toString(wifiSwitch.isChecked()))
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