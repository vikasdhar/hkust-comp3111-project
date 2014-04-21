package pacekeeper.musicplayerpackage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import android.app.Activity;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.Toast;

public class MediaList {

	
	private class Song {
		String _ID = "";
		String title = "";
		String artist = "";
		String duration = "";
		String path = "";
		String album = "";
		String albumArt = "";

		public Song(String _ID, String title, String artist, String duration,
				String path, String album, String albumArt) {
			this._ID = _ID;
			this.title = title;
			this.artist = artist;
			this.duration = duration;
			this.path = path;
			this.album = album;
			this.albumArt = albumArt;
		}
	}

	private ArrayList<String> songsList;
	private Song song;
	private HashMap<Integer, Song> cachedSongs;
	private Activity activity;

	public MediaList(Activity activity, String filter) {
		this.activity = activity;

		String[] projection = { MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DURATION,
				MediaStore.Audio.Media.ALBUM };

		Cursor cursor = activity.managedQuery(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
				MediaStore.Audio.Media.IS_MUSIC + " != 0 &" + filter, null,
				MediaStore.Audio.Media.TITLE_KEY);

		this.songsList = new ArrayList<String>();
		this.cachedSongs = new HashMap<Integer, Song>();
		int count=0;
		while (cursor.moveToNext()) {
			String path = cursor.getString(0);
			String currentAlbum = cursor.getString(5);
			String currentAlbumArt = "";

			this.songsList.add(path);

			if (currentAlbum != null)
				currentAlbumArt = Singleton_PlayerInfoHolder.getInstance().albumsList
						.getAlbumArt(currentAlbum);

			song = new Song(cursor.getString(1), cursor.getString(2),
					cursor.getString(3), cursor.getString(4), path,
					currentAlbum, currentAlbumArt);

			int key = this.songsList.indexOf(path);

			if (!cachedSongs.containsKey(key))
				cachedSongs.put(key, song);
		}
//		// Sorting
//		Collections.sort(songsList, new Comparator<songKey>() {
//			
//			@Override
//			public int compare(songKey arg0, songKey arg1) {
//				return arg0.path.compareTo(arg1.path);
//			}
//		});
	}

	public int findKeybyPath(String path) {
//		int index = Collections.binarySearch(songsList, path);
//		if (index < 0)
//			return -1;
		return  this.songsList.indexOf(path);
	}

	public String matchWithAlbum(String album) {
		String gottenAlbum = null;
		for (int i = 0; i < songsList.size(); i++) {
			gottenAlbum = cachedSongs.get(i).album;
			if (gottenAlbum.equals(album)){		
				return songsList.get(i);
			}
		}
		return null;
	}

	private int adjustPos(int position) {
		if (position < 0)
			position = songsList.size() - 1;
		else
			position %= songsList.size();
		return position;
	}

	public String getID(int position) {
		return cachedSongs.get(position)._ID;
	}

	public String getTitle(int position) {
		return cachedSongs.get(position).title;
	}

	public String getArtist(int position) {
		return cachedSongs.get(position).artist;
	}

	public double getDuration(int position) {
		String dur = cachedSongs.get(position).duration;
		Long durationInMs = (long) 0.0;
		if (dur!=null)durationInMs = Long.parseLong(dur);
		
		double durationInMin = ((double) durationInMs / 1000.0) / 60.0;

		durationInMin = new BigDecimal(Double.toString(durationInMin))
				.setScale(2, BigDecimal.ROUND_UP).doubleValue();
		return durationInMin;
	}

	public String getPath(int position) {
		return this.songsList.get(position);
	}

	public String getAlbum(int position) {
		return cachedSongs.get(position).album;
	}

	public String getAlbumArt(int position) {
		return cachedSongs.get(position).albumArt;
	}

	public boolean existKey(String path) {
		if (findKeybyPath(path) != -1)
			return true;
		else
			return false;
	}

	public String getID(String path) {
		int pos = findKeybyPath(path);
		if (pos != -1)
			return cachedSongs.get(pos)._ID;
		else
			return null;
	}

	public String getTitle(String path) {
		int pos = findKeybyPath(path);
		if (pos != -1)
			return cachedSongs.get(pos).title;
		else
			return null;
	}

	public String getArtist(String path) {
		int pos = findKeybyPath(path);
		if (pos != -1)
			return cachedSongs.get(pos).artist;
		else
			return null;
	}

	public double getDuration(String path) {
		int pos = findKeybyPath(path);
		if (pos == -1)
			return 0;
		else {
			String dur = cachedSongs.get(pos).title;
			Long durationInMs = (long) 0.0;
			if (dur!=null)durationInMs = Long.parseLong(dur);
			
			double durationInMin = ((double) durationInMs / 1000.0) / 60.0;

			durationInMin = new BigDecimal(Double.toString(durationInMin))
					.setScale(2, BigDecimal.ROUND_UP).doubleValue();
			return durationInMin;
		}
	}

	public String getPath(String path) {
		int pos = findKeybyPath(path);
		if (pos != -1)
			return cachedSongs.get(pos).path;
		else
			return null;
	}

	public String getAlbum(String path) {
		int pos = findKeybyPath(path);
		if (pos != -1)
			return cachedSongs.get(pos).album;
		else
			return null;
	}

	public String getAlbumArt(String path) {
		int pos = findKeybyPath(path);
		if (pos != -1)
			return cachedSongs.get(pos).albumArt;
		else
			return null;
	}
	
	public String nextFile(String currentFile){
		int pos=adjustPos(songsList.indexOf(currentFile)+1);
		return songsList.get(pos);
	}
	public String prevFile(String currentFile){
		int pos=adjustPos(songsList.indexOf(currentFile)-1);
		return songsList.get(pos);
	}
}