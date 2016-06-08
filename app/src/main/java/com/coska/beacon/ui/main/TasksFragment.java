package com.coska.beacon.ui.main;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coska.beacon.R;
import com.coska.beacon.model.BeaconProvider;
import com.coska.beacon.model.TaskCursorLoader;
import com.coska.beacon.model.entity.Task;
import com.coska.beacon.ui.task.TaskActivity;

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
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		super.onCreateLoader(id, args);
		return new TaskCursorLoader(getContext());
	}

	@Override
	public void onClick(View view) {

		Cursor cursor = ((Adapter) recyclerView.getAdapter()).cursor;
		if(cursor.moveToPosition(recyclerView.getChildAdapterPosition(view))) {

			long id = cursor.getLong(cursor.getColumnIndex(Task._ID));
			TaskActivity.startActivity(getActivity(), id);
		}
	}

	private static final class TasksAdapter extends Adapter<ViewHolder> {

		private final int name;
		private TasksAdapter(Cursor cursor, View.OnClickListener listener) {
			super(cursor, listener);
			name = cursor.getColumnIndex(Task.name);
		}

		@Override
		public int getItemViewType(int position) {
			return R.layout.base_list_item1;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
			holder.icon.setImageResource(getIcon(cursor.getPosition()));
			holder.text1.setText(cursor.getString(name));
		}
	}

	private static final class ViewHolder extends RecyclerView.ViewHolder {

		private final ImageView icon;
		private final TextView text1;

		public ViewHolder(View itemView) {
			super(itemView);

			icon = (ImageView) itemView.findViewById(android.R.id.icon);
			text1 = (TextView) itemView.findViewById(android.R.id.text1);
		}
	}
}