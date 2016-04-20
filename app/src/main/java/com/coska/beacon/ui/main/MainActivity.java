package com.coska.beacon.ui.main;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coska.beacon.R;
import com.coska.beacon.model.entity.Beacon;
import com.coska.beacon.ui.base.BaseActivity;

import java.util.UUID;

import static android.provider.BaseColumns._ID;
import static com.coska.beacon.model.BeaconProvider.AUTHORITY;
import static com.coska.beacon.model.BeaconProvider.PATH_BEACON;
import static com.coska.beacon.model.BeaconProvider.SCHEME;

public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final Uri beaconUri = Uri.parse(SCHEME + AUTHORITY + "/" + PATH_BEACON);
	private static final int LOADER_ID = 1;

	private RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		recyclerView = (RecyclerView) findViewById(android.R.id.list);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setHasFixedSize(true);

		getSupportLoaderManager().initLoader(LOADER_ID, null, this);
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

				getContentResolver().insert(beaconUri, cv);
				return true;
			}

			case R.id.update: {
				ContentValues cv = new ContentValues();
				cv.put(Beacon.name, Long.toString(Math.abs(System.currentTimeMillis())));

				Uri beaconWithId = beaconUri.buildUpon()
						.appendPath(String.valueOf(recyclerView.getAdapter().getItemId(0)))
						.build();
				getContentResolver().update(beaconWithId, cv, null, null);
				return true;
			}

			case R.id.delete: {
				Uri beaconWithId = beaconUri.buildUpon()
						.appendPath(String.valueOf(recyclerView.getAdapter().getItemId(0)))
						.build();
				getContentResolver().delete(beaconWithId, null, null);
				return true;
			}

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(this, beaconUri, null, null, null, _ID + " DESC");
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
}