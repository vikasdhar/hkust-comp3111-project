/**
 * 
 */
package pacekeeper.musicplayerpackage;

import pacekeeper.musicplayerpackage.R;

import android.support.v4.app.ListFragment;
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
public class Tab3Fragment extends ListFragment {
	private MediaCursorAdapter mediaAdapter = null;
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
		View view=(LinearLayout)inflater.inflate(R.layout.tab_frag3_layout, container, false);
	
		Cursor cursor = view.getContext().getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				null);

		if (null != cursor) {
			cursor.moveToFirst();

			mediaAdapter = new MediaCursorAdapter(view.getContext(), R.layout.listitem,
					cursor);

			setListAdapter(mediaAdapter);
		
		
	}
		return view;
	}
	

}
