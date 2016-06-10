package com.coska.beacon.ui.main;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coska.beacon.R;
import com.coska.beacon.model.BeaconProvider;
import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.model.entity.Task;
import com.coska.beacon.ui.task.TaskActivity;

import static android.graphics.Typeface.BOLD;

public class TasksFragment extends BaseListFragment implements View.OnClickListener {

	@Override
	protected Adapter getAdapter(Cursor cursor) {
		return new TasksAdapter(cursor, this);
	}

	@Override
	protected Uri getUri() {
		return BeaconProvider.buildUri(BeaconProvider.PATH_TASK);
	}

	@Override
	public void onClick(View view) {

		Cursor cursor = ((Adapter) recyclerView.getAdapter()).cursor;
		if(cursor.moveToPosition(recyclerView.getChildAdapterPosition(view))) {

			long taskId = cursor.getLong(cursor.getColumnIndex(Task._ID));
			long beaconId = cursor.getLong(cursor.getColumnIndex(Beacon._table + Beacon._ID));
			TaskActivity.startActivity(getActivity(), taskId, beaconId);
		}
	}

	private static final class TasksAdapter extends Adapter<ViewHolder> {

		private final int name;
		private final int rules;
		private final int actions;
		private final int beaconName;

		private TasksAdapter(Cursor cursor, View.OnClickListener listener) {
			super(cursor, listener);
			name = cursor.getColumnIndex(Task.name);
			rules = cursor.getColumnIndex("rules");
			actions = cursor.getColumnIndex("actions");
			beaconName = cursor.getColumnIndex("beacon_name");
		}

		@Override
		public int getItemViewType(int position) {
			return R.layout.base_list_item2;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
			holder.icon.setImageResource(getIcon(cursor.getPosition()));
			holder.text1.setText(cursor.getString(name));

			SpannableStringBuilder builder = new SpannableStringBuilder();
			builder.append("Rules: ").append(cursor.getString(rules) == null ? "0" : cursor.getString(rules))
					.setSpan(new StyleSpan(BOLD), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			int length = builder.length();
			builder.append(", Action: ").append(cursor.getString(actions) == null ? "0" : cursor.getString(actions))
					.setSpan(new StyleSpan(BOLD), length+2, length+8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			length = builder.length();
			builder.append("\nBeacon: ").append(cursor.getString(beaconName))
					.setSpan(new StyleSpan(BOLD), length, length+7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.text2.setText(builder);
		}
	}

	private static final class ViewHolder extends RecyclerView.ViewHolder {

		private final ImageView icon;
		private final TextView text1;
		private final TextView text2;

		public ViewHolder(View itemView) {
			super(itemView);

			icon = (ImageView) itemView.findViewById(android.R.id.icon);
			text1 = (TextView) itemView.findViewById(android.R.id.text1);
			text2 = (TextView) itemView.findViewById(android.R.id.text2);
			text2.setMaxLines(2);
		}
	}
}