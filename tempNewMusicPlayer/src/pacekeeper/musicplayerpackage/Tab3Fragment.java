/**
 * 
 */
package pacekeeper.musicplayerpackage;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 *
 */
public class Tab3Fragment extends ListFragment {
	private AlbumsCursorAdapter albumsAdapter = null;
	private MusicPlayer_with_SongsLists activity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MusicPlayer_with_SongsLists) activity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (container == null) {

			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist. The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed. Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}
		View view = (LinearLayout) inflater.inflate(R.layout.tab_frag3_layout,
				container, false);

		Cursor cursor = view
				.getContext()
				.getContentResolver()
				.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null,
						null, null, MediaStore.Audio.Albums.ALBUM_KEY);

		if (null != cursor) {
			cursor.moveToFirst();

			albumsAdapter = new AlbumsCursorAdapter(activity,
					view.getContext(), R.layout.listitem, cursor);

			setListAdapter(albumsAdapter);

		}
		return view;
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);

		// Create new fragment and transaction
		Fragment newFragment = new TestFragment();
		FragmentTransaction transaction = getChildFragmentManager()
				.beginTransaction();
		// Replace whatever is in
		// the fragment_container view with this fragment, // and add the
		// transaction to the back stack
		transaction.replace(R.id.frag3, newFragment);
		transaction.addToBackStack(null); // Commit the transaction
		transaction.commit();
	}

}
