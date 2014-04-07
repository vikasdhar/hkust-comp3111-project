package pacekeeper.musicplayerpackage;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.Toast;

public class MediaList {
	private static final int LENGHT_LONG = 0;
	private static final int LENGTH_LONG = 0;
	ArrayList<ArrayList<String>> songsList;
	private Activity activity;

	public MediaList(Activity activity, String sortedBy) {
		String[] projection1 = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.ALBUM
				 };

				
		String[] projection2 = {
				
				MediaStore.Audio.Albums.ALBUM,
				MediaStore.Audio.Albums.ALBUM_ART };
		
		
		Cursor cursor = activity.managedQuery(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection1,
				MediaStore.Audio.Media.IS_MUSIC + " != 0", null, sortedBy);

		this.songsList = new ArrayList<ArrayList<String>>();
		while (cursor.moveToNext()) {
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(cursor.getString(0));
			temp.add(cursor.getString(1));
			temp.add(cursor.getString(2));
			temp.add(cursor.getString(3));
			temp.add(cursor.getString(4));
			temp.add(cursor.getString(5));
			temp.add(cursor.getString(6));
			temp.add("");
			this.songsList.add(temp);
		}

		Cursor cursor2 = activity.managedQuery(
				MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, projection2,
				null, null, MediaStore.Audio.Albums.ALBUM_KEY);
		
		while (cursor2.moveToNext()) {
			for (int i = 0; i < songsList.size(); i++) {
				
				if (songsList.get(i).get(6).equals(cursor2.getString(0))) {
					
					songsList.get(i).set(7,cursor2.getString(1));
				}
			}
		}
	}

	public int matchWithName(String currentFile) {
		int pos = -1;
		for (int i = 0; i < songsList.size(); i++) {
			if (songsList.get(i).get(4).equals(currentFile))
				pos = i;
		}
		return pos;
	}

	public String getID(int position) {
		if (position < 0)
			position = songsList.size() - 1;
		else
			position %= songsList.size();
		return songsList.get(position).get(0);
	}

	public String getTitle(int position) {
		if (position < 0)
			position = songsList.size() - 1;

		else
			position %= songsList.size();

		return songsList.get(position).get(1);
	}

	public String getArtist(int position) {
		if (position < 0)
			position = songsList.size() - 1;
		else
			position %= songsList.size();
		return songsList.get(position).get(2);
	}

	public double getDuration(int position) {
		if (position < 0)
			position = songsList.size() - 1;
		else
			position %= songsList.size();
		Long durationInMs = Long.parseLong(songsList.get(position).get(3));
		double durationInMin = ((double) durationInMs / 1000.0) / 60.0;

		durationInMin = new BigDecimal(Double.toString(durationInMin))
				.setScale(2, BigDecimal.ROUND_UP).doubleValue();
		return durationInMin;
	}

	public String getPath(int position) {
		if (position < 0)
			position = songsList.size() - 1;
		else
			position %= songsList.size();
		return songsList.get(position).get(4);
	}
	
	public String getArt(int position) {
		if (position < 0)
			position = songsList.size() - 1;
		else
			position %= songsList.size();
		return songsList.get(position).get(7);
	}

}