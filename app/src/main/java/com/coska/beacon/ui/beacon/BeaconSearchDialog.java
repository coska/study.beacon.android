package com.coska.beacon.ui.beacon;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatDialogFragment;
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
import com.coska.beacon.model.SignalCursorLoader;
import com.coska.beacon.model.entity.Signal;

public class BeaconSearchDialog extends AppCompatDialogFragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

	public static void show(FragmentManager manager) {
		new BeaconSearchDialog().show(manager, BeaconSearchDialog.class.getName());
	}

	private static final String target = BeaconFragment.class.getName();

	private RecyclerView recyclerView;
	private ProgressBar progressBar;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		getDialog().setTitle("Searching Beacons");
		return inflater.inflate(R.layout.base_list_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		recyclerView = (RecyclerView) view.findViewById(android.R.id.list);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setHasFixedSize(true);

		progressBar = (ProgressBar) view.findViewById(android.R.id.progress);

		getLoaderManager().initLoader(hashCode(), null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		progressBar.setVisibility(View.VISIBLE);
//		return new CursorLoader(getContext(), BeaconProvider.buildUri(BeaconProvider.PATH_SIGNAL), null, null, null, null);
		return new SignalCursorLoader(getContext());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		progressBar.setVisibility(View.GONE);
		recyclerView.swapAdapter(new Adapter(cursor, this), false);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) { }

	@Override
	public void onClick(View view) {
		Cursor cursor = ((Adapter) recyclerView.getAdapter()).cursor;
		if(cursor.moveToPosition(recyclerView.getChildAdapterPosition(view))) {

			((BeaconFragment) getFragmentManager().findFragmentByTag(target)).fillInput(cursor);
			dismiss();
		}
	}

	private static final class Adapter extends RecyclerView.Adapter<ViewHolder> {

		protected final Cursor cursor;

		private final int uuid;
		private final int major;
		private final int minor;

		private final int telemetry;
		private final int distance;
		private final int battery;
		private final int pduCount;
		private final int uptime;

		private View.OnClickListener listener;

		private Adapter(Cursor cursor, View.OnClickListener listener) {
			this.cursor = cursor;

			this.uuid = cursor.getColumnIndex(Signal.uuid);
			this.major = cursor.getColumnIndex(Signal.major);
			this.minor = cursor.getColumnIndex(Signal.minor);

			this.telemetry = cursor.getColumnIndex(Signal.telemetry);
			this.distance = cursor.getColumnIndex(Signal.distance);
			this.battery = cursor.getColumnIndex(Signal.battery);
			this.pduCount = cursor.getColumnIndex(Signal.pduCount);
			this.uptime = cursor.getColumnIndex(Signal.uptime);

			this.listener = listener;
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
				StringBuilder title = new StringBuilder(cursor.getString(uuid));

				String text = cursor.getString(major);
				if(!TextUtils.isEmpty(text)) {
					title.append(".").append(text);
				}

				text = cursor.getString(minor);
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