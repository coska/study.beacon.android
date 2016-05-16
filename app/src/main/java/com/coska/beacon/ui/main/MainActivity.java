package com.coska.beacon.ui.main;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.Menu;

import com.coska.beacon.R;
import com.coska.beacon.model.BeaconProvider;
import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.ui.base.BaseActivity;

import java.util.UUID;


public class MainActivity extends BaseActivity { // implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

	//private static final int LOADER_ID = 1;

//	private RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		showFragment(new MainFragment(), R.id.fragment_container, true, false);

		/*
		recyclerView = (RecyclerView) findViewById(android.R.id.list);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setHasFixedSize(true);

		getSupportLoaderManager().initLoader(LOADER_ID, null, this);

		//noinspection ConstantConditions
		findViewById(R.id.new_task_floating_btn).setOnClickListener(this);
		*/
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		overridePendingTransition(0, 0);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

			case R.id.create: {
				ContentValues cv = new ContentValues();
				cv.put(Beacon.name, Long.toString(Math.abs(System.currentTimeMillis())));
				cv.put(Beacon.uuid, UUID.randomUUID().toString());

				getContentResolver().insert(BeaconProvider.buildUri(BeaconProvider.PATH_BEACON), cv);
				return true;
			}

			case R.id.update: {
				ContentValues cv = new ContentValues();
				cv.put(Beacon.name, Long.toString(Math.abs(System.currentTimeMillis())));

//				Uri uri = BeaconProvider.buildUri(BeaconProvider.PATH_BEACON, recyclerView.getAdapter().getItemId(0));
//				getContentResolver().update(uri, cv, null, null);
				return true;
			}

			case R.id.delete: {
//				Uri uri = BeaconProvider.buildUri(BeaconProvider.PATH_BEACON, recyclerView.getAdapter().getItemId(0));
//				getContentResolver().delete(uri, null, null);
				return true;
			}

			default:
				return super.onOptionsItemSelected(item);
		}
	}
/*
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(this, BeaconProvider.buildUri(PATH_BEACON), null, null, null, _ID + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		recyclerView.swapAdapter(new Adapter(cursor), false);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) { }

	private static final class Adapter extends RecyclerView.Adapter<ViewHolder> {

		private final Cursor cursor;
		private Adapter(Cursor cursor) {
			setHasStableIds(true);
			this.cursor = cursor;
		}

		@Override
		public long getItemId(int position) {
			if(cursor.moveToPosition(position)) {
				return cursor.getLong(cursor.getColumnIndex(_ID));

			} else {
				return -1;
			}
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			return new ViewHolder(inflater.inflate(android.R.layout.simple_list_item_1, parent, false));
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, int position) {
			if(cursor.moveToPosition(position)) {
				String text = DatabaseUtils.dumpCurrentRowToString(cursor);
				((TextView) holder.itemView).setText(text);
			}
		}

		@Override
		public int getItemCount() {
			return cursor == null ? 0 : cursor.getCount();
		}
	}

	private static final class ViewHolder extends RecyclerView.ViewHolder {

		public ViewHolder(View itemView) {
			super(itemView);
		}
	}
	*/
}