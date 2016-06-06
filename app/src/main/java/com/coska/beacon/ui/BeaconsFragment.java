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
import com.coska.beacon.model.BeaconCursorLoader;
import com.coska.beacon.model.BeaconProvider;
import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.ui.base.BaseListFragment;

public class BeaconsFragment extends BaseListFragment implements View.OnClickListener {

	public BeaconsFragment() {
		setHasOptionsMenu(true);
	}

	@Override
	protected Adapter getAdapter(Cursor cursor) {
		return new BeaconsAdapter(cursor, this);
	}

	@Override
	protected Uri getUri() {
		return BeaconProvider.buildUri(BeaconProvider.PATH_BEACON);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		super.onCreateLoader(id, args);
		return new BeaconCursorLoader(getContext());
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_add, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.add:
				String tag = BeaconFragment.class.getName();
				beginTrasaction()
						.add(android.R.id.content, BeaconFragment.getInstance(), tag)
						.remove(this)
						.addToBackStack(tag)
						.commit();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View view) {

		Cursor cursor = ((Adapter) recyclerView.getAdapter()).cursor;
		if(cursor.moveToPosition(recyclerView.getChildAdapterPosition(view))) {

			String uuid = cursor.getString(cursor.getColumnIndex(Beacon.uuid));
			String tag = BeaconFragment.class.getName();

			beginTrasaction()
					.add(android.R.id.content, BeaconFragment.getInstance(uuid), tag)
					.remove(this)
					.addToBackStack(tag)
					.commit();
		}
	}

	private static final class BeaconsAdapter extends Adapter<ViewHolder> {

		private final int name;
		private final int uuid;

		private View.OnClickListener listener;

		private BeaconsAdapter(Cursor cursor, View.OnClickListener listener) {
			super(cursor);

			this.name = cursor.getColumnIndex(Beacon.name);
			this.uuid = cursor.getColumnIndex(Beacon.uuid);

			this.listener = listener;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, null));
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
			holder.text1.setText(cursor.getString(name));
			holder.text2.setText(cursor.getString(uuid));
		}

		@Override
		public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
			listener = null;
		}

		@Override
		public void onViewAttachedToWindow(ViewHolder holder) {
			holder.itemView.setOnClickListener(listener);
		}

		@Override
		public void onViewDetachedFromWindow(ViewHolder holder) {
			holder.itemView.setOnClickListener(null);
		}
	}

	private static final class ViewHolder extends RecyclerView.ViewHolder {

		private final TextView text1;
		private final TextView text2;

		public ViewHolder(View itemView) {
			super(itemView);

			text1 = (TextView) itemView.findViewById(android.R.id.text1);
			text2 = (TextView) itemView.findViewById(android.R.id.text2);
		}
	}
}