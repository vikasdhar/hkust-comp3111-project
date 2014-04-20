package pacekeeper.musicplayerpackage;
import java.math.BigDecimal;

import pacekeeper.musicplayerpackage.R;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



public class MediaCursorAdapter extends SimpleCursorAdapter {

		
		private Activity mainActivity;
		private Context context;
		private Cursor cursor;
		private LayoutInflater inflater;
		
		public MediaCursorAdapter(Activity activity, Context context, int layout, Cursor c) {
			super(context, layout, c, new String[] {
					MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media.ARTIST,
					MediaStore.Audio.Media.DURATION,
					MediaStore.Audio.Media.DATA,
					MediaStore.Audio.Media.TITLE_KEY}, new int[] {
					R.id.displayname, R.id.title, R.id.duration });
			
			this.mainActivity = activity;
			this.context = context;
			this.cursor = c;
			this.inflater = LayoutInflater.from(context);
		}

//		@Override
//		public void bindView(View view, Context context, Cursor cursor) {
//			TextView title = (TextView) view.findViewById(R.id.title);
//			TextView name = (TextView) view.findViewById(R.id.displayname);
//			TextView duration = (TextView) view.findViewById(R.id.duration);
//			ImageView albumArt = (ImageView) view.findViewById(R.id.smallalbumart);
//			
//			albumArt.setImageDrawable(context.getResources().getDrawable(
//					R.drawable.ic_expandplayer_placeholder));
//			
//			name.setText(cursor.getString(cursor
//					.getColumnIndex(MediaStore.Audio.Media.TITLE)));
//
//			title.setText(cursor.getString(cursor
//					.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
//
//			long durationInMs = Long.parseLong(cursor.getString(cursor
//					.getColumnIndex(MediaStore.Audio.Media.DURATION)));
//
//			double durationInMin = ((double) durationInMs / 1000.0) / 60.0;
//
//			durationInMin = new BigDecimal(Double.toString(durationInMin))
//					.setScale(2, BigDecimal.ROUND_UP).doubleValue();
//
//			duration.setText("" + durationInMin);
//
//			view.setTag(cursor.getString(cursor
//					.getColumnIndex(MediaStore.Audio.Media.DATA)));
//			
//			String file= cursor.getString(cursor
//					.getColumnIndex(MediaStore.Audio.Media.DATA));
//			
//			((MusicPlayer_with_SongsLists) mainActivity).setAlbumArt(albumArt,file,true);
//		}

//		@Override
//		public View newView(Context context, Cursor cursor, ViewGroup parent) {
//			LayoutInflater inflater = LayoutInflater.from(context);
//			View v = inflater.inflate(R.layout.listitem, parent, false);
//			
//			bindView(v, context, cursor);
//
//			return v;
//		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			MediaViewHolder holder;
			super.getView(position, convertView, parent);
			if (convertView == null) {
				//convertView = inflater.inflate(R.layout.listitem, parent, false);
				convertView = inflater.inflate(R.layout.listitem, null);
				holder = new MediaViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.name = (TextView) convertView.findViewById(R.id.displayname);
				holder.duration = (TextView) convertView.findViewById(R.id.duration);
				holder.albumArt = (ImageView) convertView.findViewById(R.id.smallalbumart);
				convertView.setTag(holder);

			} else {
				holder = (MediaViewHolder) convertView.getTag();
			}
			
			
			holder.name.setText(cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.TITLE)));

			holder.title.setText(cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST)));

			String tempDur= cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DURATION));
			
			long durationInMs = 0;
			if (tempDur != null) durationInMs=Long.parseLong(tempDur);

			double durationInMin = ((double) durationInMs / 1000.0) / 60.0;

			durationInMin = new BigDecimal(Double.toString(durationInMin))
					.setScale(2, BigDecimal.ROUND_UP).doubleValue();

			holder.duration.setText("" + durationInMin);
			
			String filePath= cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA));
			
			holder.path=filePath;
			
			PlayerInfoHolder.setAlbumArt(holder.albumArt,filePath,true);
			
			return convertView;
		}
		
		 class MediaViewHolder {
			TextView title ;
			TextView name ;
			TextView duration ;
			ImageView albumArt ;
			String path;
		}
	}