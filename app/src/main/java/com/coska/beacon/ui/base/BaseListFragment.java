package com.coska.beacon.ui.base;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coska.beacon.R;

public abstract class BaseListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final int LOADER_ID = ++_internal_loader_count;

	protected RecyclerView recyclerView;
	private ProgressBar progressBar;

	protected TextView message;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.beacon_list_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		recyclerView = (RecyclerView) view.findViewById(android.R.id.list);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setHasFixedSize(true);

		progressBar = (ProgressBar) view.findViewById(android.R.id.progress);
		message = (TextView) view.findViewById(android.R.id.message);

		getLoaderManager().initLoader(LOADER_ID, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		progressBar.setVisibility(View.VISIBLE);
		message.setVisibility(View.GONE);

		return new CursorLoader(getContext(), getUri(), null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		progressBar.setVisibility(View.GONE);
		message.setVisibility(cursor.getCount() == 0 ? View.VISIBLE : View.GONE);

		recyclerView.swapAdapter(getAdapter(cursor), false);
	}

	protected abstract Adapter getAdapter(Cursor cursor);
	protected abstract Uri getUri();

	@Override
	public void onLoaderReset(Loader<Cursor> loader) { }

	public static abstract class Adapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

		public final Cursor cursor;
		protected Adapter(Cursor cursor) {
			this.cursor = cursor;
		}

		@Override
		public int getItemViewType(int position) {
			return android.R.layout.simple_list_item_2;
		}

		@Override
		public void onBindViewHolder(VH holder, int position) {
			if(cursor.moveToPosition(position)) {
				onBindViewHolder(holder, cursor);
			}
		}

		protected abstract void onBindViewHolder(VH holder, Cursor cursor);

		@Override
		public int getItemCount() {
			return cursor.getCount();
		}
	}
}