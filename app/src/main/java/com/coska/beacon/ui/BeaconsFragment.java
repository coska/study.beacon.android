package com.coska.beacon.ui;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
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
import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.ui.base.BaseListFragment;

public class BeaconsFragment extends BaseListFragment {

	@Override
	protected Adapter getAdapter(Cursor cursor) {
		return new BeaconsAdapter(cursor);
	}

	@Override
	protected Uri getUri() {
		return BeaconProvider.buildUri(BeaconProvider.PATH_BEACON);
	}

	public BeaconsFragment() {
		setHasOptionsMenu(true);
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
				getFragmentManager().beginTransaction()
						.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
						.add(android.R.id.content, BeaconFragment.getInstance(), tag)
						.remove(this)
						.addToBackStack(tag)
						.commit();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private static final class BeaconsAdapter extends Adapter<ViewHolder> {

		private final int name;
		private final int uuid;

		private BeaconsAdapter(Cursor cursor) {
			super(cursor);

			name = cursor.getColumnIndex(Beacon.name);
			uuid = cursor.getColumnIndex(Beacon.uuid);
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