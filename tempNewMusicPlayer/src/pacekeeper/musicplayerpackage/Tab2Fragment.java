/**
 * 
 */
package pacekeeper.musicplayerpackage;

import pacekeeper.musicplayerpackage.R;


import android.support.v4.app.ListFragment;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;



/**
 *
 */
public class Tab2Fragment extends ListFragment {
	private ArtistsCursorAdapter artistsAdapter = null;
	
private MusicPlayer_with_SongsLists activity;
	
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		this.activity=(MusicPlayer_with_SongsLists)activity;
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
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
		View view=(LinearLayout)inflater.inflate(R.layout.tab_frag2_layout, container, false);
	
		Cursor cursor = view.getContext().getContentResolver().query(
				MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Artists.ARTIST_KEY);

		if (null != cursor) {
			cursor.moveToFirst();

			artistsAdapter = new ArtistsCursorAdapter(activity,view.getContext(), R.layout.listitem,
					cursor);

			setListAdapter(artistsAdapter);
		
		
	}
		return view;
	}
	

}
