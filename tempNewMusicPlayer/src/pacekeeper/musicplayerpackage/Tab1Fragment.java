/**
 * 
 */
package pacekeeper.musicplayerpackage;

import java.math.BigDecimal;

import pacekeeper.musicplayerpackage.R;

import android.support.v4.app.ListFragment;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;



/**
 *
 */
public class Tab1Fragment extends ListFragment {
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */



	private MediaCursorAdapter mediaAdapter = null;
	private MusicPlayer_with_SongsLists activity;
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		this.activity=(MusicPlayer_with_SongsLists)activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
		View view=(LinearLayout)inflater.inflate(R.layout.tab_frag1_layout, container, false);
	
		Cursor cursor = view.getContext().getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.IS_MUSIC + " != 0", null,
				MediaStore.Audio.Media.TITLE_KEY);

		if (null != cursor) {
			cursor.moveToFirst();

			mediaAdapter = new MediaCursorAdapter(view.getContext(), R.layout.listitem,
					cursor);

			setListAdapter(mediaAdapter);
		
		
	}
		return view;
	}
	
	@Override
	public void onListItemClick(ListView list, View view, int position,
			long id) {
		super.onListItemClick(list, view, position, id);
		
		activity.currentFile = (String) view.getTag();
		
		activity.startPlay(activity.currentFile);
	}
}
