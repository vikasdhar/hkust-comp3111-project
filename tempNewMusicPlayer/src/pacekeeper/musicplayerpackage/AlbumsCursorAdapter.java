package pacekeeper.musicplayerpackage;
import java.math.BigDecimal;

import pacekeeper.musicplayerpackage.R;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class AlbumsCursorAdapter extends SimpleCursorAdapter {

		
		public AlbumsCursorAdapter(Context context, int layout, Cursor c) {
			super(context, layout, c, new String[] {
					MediaStore.Audio.Albums.ALBUM,
					MediaStore.Audio.Albums.ARTIST,
					MediaStore.Audio.Albums.NUMBER_OF_SONGS}, new int[] {
					R.id.displayname, R.id.title, R.id.duration });
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView title = (TextView) view.findViewById(R.id.title);
			TextView name = (TextView) view.findViewById(R.id.displayname);
			TextView numberofAlbums = (TextView) view.findViewById(R.id.duration);

			name.setText(cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));

			title.setText(cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Albums.ARTIST)));

			int numberOfSongs = (cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS)));

			numberofAlbums.setText("" + numberOfSongs);

//			view.setTag(cursor.getString(cursor
//					.getColumnIndex(MediaStore.Audio.Media.DATA)));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.listitem, parent, false);
			
			bindView(v, context, cursor);

			return v;
		}
	}