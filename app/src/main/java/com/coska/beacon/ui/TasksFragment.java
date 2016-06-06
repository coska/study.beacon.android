package com.coska.beacon.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coska.beacon.R;
import com.coska.beacon.model.BeaconProvider;
import com.coska.beacon.model.TaskCursorLoader;
import com.coska.beacon.model.entity.Task;
import com.coska.beacon.ui.base.BaseListFragment;
import com.coska.beacon.ui.task.TaskFragment;

public class TasksFragment extends BaseListFragment {

	public TasksFragment() {
		setHasOptionsMenu(true);
	}

	@Override
	protected Adapter getAdapter(Cursor cursor) {
		return new TasksAdapter(cursor);
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_add, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.add:
				String tag = TaskFragment.class.getName();
				beginTrasaction()
						.add(android.R.id.content, new TaskFragment(), tag)
						.remove(this)
						.addToBackStack(tag)
						.commit();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private static final class TasksAdapter extends Adapter<ViewHolder> {

		private final int name;
		private TasksAdapter(Cursor cursor) {
			super(cursor);
			name = cursor.getColumnIndex(Task.name);
		}

		@Override
		public int getItemViewType(int position) {
			return android.R.layout.simple_list_item_1;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, null));
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
			holder.text1.setText(cursor.getString(name));
		}
	}

	private static final class ViewHolder extends RecyclerView.ViewHolder {

		private final TextView text1;
		public ViewHolder(View itemView) {
			super(itemView);
			text1 = (TextView) itemView.findViewById(android.R.id.text1);
		}
	}
}