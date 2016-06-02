package com.coska.beacon.ui.test;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coska.beacon.R;
import com.coska.beacon.model.BeaconProvider;
import com.coska.beacon.model.entity.Signal;
import com.coska.beacon.ui.base.BaseFragment;

public class BeaconsFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final int LOADER_ID = 1;

	private RecyclerView recyclerView;
	private ProgressBar progressBar;

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

		getLoaderManager().initLoader(LOADER_ID, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		progressBar.setVisibility(View.VISIBLE);
		return new CursorLoader(getContext(), BeaconProvider.buildUri(BeaconProvider.PATH_SIGNAL), null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		progressBar.setVisibility(View.GONE);
		recyclerView.swapAdapter(new Adapter(cursor), false);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) { }

	private static final class Adapter extends RecyclerView.Adapter<ViewHolder> {

		private final Cursor cursor;

		private final int telemetry;
		private final int distance;
		private final int battery;
		private final int pduCount;
		private final int uptime;

		private Adapter(Cursor cursor) {
			this.cursor = cursor;

			this.telemetry = cursor.getColumnIndex(Signal.telemetry);
			this.distance = cursor.getColumnIndex(Signal.distance);
			this.battery = cursor.getColumnIndex(Signal.battery);
			this.pduCount = cursor.getColumnIndex(Signal.pduCount);
			this.uptime = cursor.getColumnIndex(Signal.uptime);
		}

		@Override
		public int getItemViewType(int position) {
			return android.R.layout.simple_list_item_2;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, null));
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, int position) {

			if(cursor.moveToPosition(position)) {
				StringBuilder title = new StringBuilder()
						.append(cursor.getString(cursor.getColumnIndex(Signal.uuid)));

				String text = cursor.getString(cursor.getColumnIndex(Signal.major));
				if(!TextUtils.isEmpty(text)) {
					title.append(".").append(text);
				}

				text = cursor.getString(cursor.getColumnIndex(Signal.minor));
				if(!TextUtils.isEmpty(text)) {
					title.append(".").append(text);
				}

				holder.text1.setText(title);

				SpannableStringBuilder builder = new SpannableStringBuilder();
				append(builder, Signal.telemetry, cursor.getString(telemetry));
				append(builder, Signal.distance, cursor.getString(distance));
				append(builder, Signal.battery, cursor.getString(battery));
				append(builder, Signal.pduCount, cursor.getString(pduCount));
				append(builder, Signal.uptime, cursor.getString(uptime));
				holder.text2.setText(builder);
			}
		}

		private void append(SpannableStringBuilder builder, String caption, String value) {

			if(TextUtils.isEmpty(value)) {
				return;
			}

			if(!TextUtils.isEmpty(builder)) {
				builder.append(", ");
			}

			final int length = builder.append(caption).append(" ").length();
			builder.append(value)
					.setSpan(new ForegroundColorSpan(Color.GRAY), length, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		@Override
		public int getItemCount() {
			return cursor.getCount();
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