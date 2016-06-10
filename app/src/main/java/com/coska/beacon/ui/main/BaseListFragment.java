package com.coska.beacon.ui.main;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coska.beacon.R;
import com.coska.beacon.ui.base.BaseFragment;

public abstract class BaseListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final int LOADER_ID = ++_internal_loader_count;

	private CoordinatorLayout coordinator;
	protected RecyclerView recyclerView;
	private ProgressBar progressBar;

	protected TextView message;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		coordinator = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.base_list_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		recyclerView = (RecyclerView) view.findViewById(android.R.id.list);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setHasFixedSize(true);

		new ItemTouchHelper(new SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
			@Override
			public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(ViewHolder viewHolder, int swipeDir) {
				onDelete(getUri().buildUpon()
						.appendPath(Long.toString(viewHolder.getItemId()))
						.build());
			}
		}).attachToRecyclerView(recyclerView);

		progressBar = (ProgressBar) view.findViewById(android.R.id.progress);
		message = (TextView) view.findViewById(android.R.id.message);

		getLoaderManager().initLoader(LOADER_ID, null, this);
	}

	protected void onDelete(Uri uri) {
		getContext().getContentResolver().delete(uri, null, null);
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

		protected final Cursor cursor;
		private View.OnClickListener listener;

		private final int id;

		protected Adapter(Cursor cursor, View.OnClickListener listener) {
			setHasStableIds(true);
			this.cursor = cursor;
			this.listener = listener;

			this.id = cursor.getColumnIndex(BaseColumns._ID);
		}

		@Override
		public void onBindViewHolder(VH holder, int position) {
			if(cursor.moveToPosition(position)) {
				onBindViewHolder(holder, cursor);
			}
		}

		protected abstract void onBindViewHolder(VH holder, Cursor cursor);

		@Override
		public long getItemId(int position) {
			return cursor.moveToPosition(position) ? cursor.getLong(id) : -1;
		}

		@Override
		public int getItemCount() {
			return cursor.getCount();
		}

		@Override
		public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
			listener = null;
		}

		@Override
		public void onViewAttachedToWindow(VH holder) {
			holder.itemView.setOnClickListener(listener);
		}

		@Override
		public void onViewDetachedFromWindow(VH holder) {
			holder.itemView.setOnClickListener(null);
		}

		protected int getIcon(int position) {
			switch ((int) (getItemId(position)%10)) {
				default:
				case 0: return R.mipmap.a0;
				case 1: return R.mipmap.a1;
				case 2: return R.mipmap.a2;
				case 3: return R.mipmap.a3;
				case 4: return R.mipmap.a4;
				case 5: return R.mipmap.a5;
				case 6: return R.mipmap.a6;
				case 7: return R.mipmap.a7;
				case 8: return R.mipmap.a8;
				case 9: return R.mipmap.a9;
			}
		}
	}
}