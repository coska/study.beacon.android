package com.coska.beacon.ui.main;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coska.beacon.R;
import com.coska.beacon.model.BeaconCursorLoader;
import com.coska.beacon.model.BeaconProvider;
import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.ui.base.BaseListFragment;
import com.coska.beacon.ui.beacon.BeaconActivity;

public class BeaconsFragment extends BaseListFragment implements View.OnClickListener {

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
	public void onClick(View view) {

		Cursor cursor = ((Adapter) recyclerView.getAdapter()).cursor;
		if(cursor.moveToPosition(recyclerView.getChildAdapterPosition(view))) {

			String uuid = cursor.getString(cursor.getColumnIndex(Beacon.uuid));
			BeaconActivity.startActivity(getActivity(), uuid);
		}
	}

	private static final class BeaconsAdapter extends Adapter<ViewHolder> {

		private final int name;
		private final int uuid;

		private BeaconsAdapter(Cursor cursor, View.OnClickListener listener) {
			super(cursor, listener);

			this.name = cursor.getColumnIndex(Beacon.name);
			this.uuid = cursor.getColumnIndex(Beacon.uuid);
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
			holder.text1.setText(cursor.getString(name));
			holder.text2.setText(cursor.getString(uuid));
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