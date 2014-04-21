package pacekeeper.musicplayerpackage;

import android.support.v4.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SongsFromAlbumFragment extends ListFragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		View view=(LinearLayout)inflater.inflate(R.layout.songsfromalbum_frag_layout, container, false);
		
		Cursor cursor = view
				.getContext()
				.getContentResolver()
				.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
						MediaStore.Audio.Media.IS_MUSIC + " != 0 & "+MediaStore.Audio.Media.ALBUM+" = 'album'", null,
						MediaStore.Audio.Media.TITLE_KEY);

		if (null != cursor) {
			cursor.moveToFirst();

			MediaCursorAdapter mediaAdapter = new MediaCursorAdapter(Tab3Fragment.activity, view.getContext(),
					R.layout.listitem, cursor);

			setListAdapter(mediaAdapter);

		}
	
		return view;
	}
	
	
	@Override
	public void onDestroy(){
	
		Singleton_TabInfoHolder tabInfoHolder=Singleton_TabInfoHolder.getInstance();
		
		Tab3Fragment.myListView.getLayoutParams().height = Tab3Fragment.height;
		Tab3Fragment.myListView.getLayoutParams().width = Tab3Fragment.width;
		Tab3Fragment.myListView.requestLayout();
		
		Tab3Fragment.fragView.getLayoutParams().height =0;
		Tab3Fragment.fragView.getLayoutParams().width = 0;
		
		tabInfoHolder.setPageSwipeable(true);
		tabInfoHolder.setTabClickable(true);
		
		super.onDestroy();
		
	}
}
