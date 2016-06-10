package com.coska.beacon.ui.task.rule;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import com.coska.beacon.ui.task.EntityView;

import org.json.JSONException;

public abstract class RuleView extends CardView implements EntityView {

	public RuleView(Context context) {
		super(context);
	}

	public RuleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RuleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public abstract boolean validate();
	public abstract void setConfiguration(long id, String configuration) throws JSONException;
}