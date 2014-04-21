/**
 * 
 */
package pacekeeper.musicplayerpackage;

import pacekeeper.musicplayerpackage.AlbumsCursorAdapter.AlbumsViewHolder;
import pacekeeper.musicplayerpackage.MediaCursorAdapter.MediaViewHolder;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 */
public class ListFragment_SortByAlbum extends ListFragment {

	private static MainMusicPlayerActivity activity;
	private Singleton_TabInfoHolder tabInfoHolder = Singleton_TabInfoHolder
			.getInstance();

	private static View fragView;
	private static View myListView;
	private static Cursor cursor;
	private AlbumsCursorAdapter albumsAdapter = null;

	private FragmentTransaction transaction;
	private Fragment childFragment;
	private static int height;
	private static int width;
	private static String albumTitle;

	public static class ListFragment_SortByAlbumChild extends ListFragment {
		private TextView albumIndicater;
		private ImageButton playAllButton;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View view = (LinearLayout) inflater.inflate(
					R.layout.lf_sortbyalbumchild_layout, container, false);

			String whereValue[] = { albumTitle };

			Cursor cursor = view
					.getContext()
					.getContentResolver()
					.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
							null,
							MediaStore.Audio.Media.IS_MUSIC + " != 0 and "
									+ MediaStore.Audio.Media.ALBUM + " =?",
							whereValue, MediaStore.Audio.Media.TITLE_KEY);

			if (null != cursor) {
				cursor.moveToFirst();

				MediaCursorAdapter mediaAdapter = new MediaCursorAdapter(
						ListFragment_SortByAlbum.activity, view.getContext(),
						R.layout.musicplayer_listitem, cursor);

				setListAdapter(mediaAdapter);

			}

			albumIndicater=(TextView) view.findViewById(R.id.sortByAlbumChild_albumIndicator);
			albumIndicater.setText(albumTitle);
			playAllButton=(ImageButton) view.findViewById(R.id.sortByAlbumChild_playAll);
			playAllButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
				
				}
			});
			return view;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			SwipeListViewTouchListener touchListener = new SwipeListViewTouchListener(
					getListView(),
					new SwipeListViewTouchListener.OnSwipeCallback() {
						@Override
						public void onSwipeLeft(ListView listView,
								int[] reverseSortedPositions) {
							// Log.i(this.getClass().getName(),
							// "swipe left : pos="+reverseSortedPositions[0]);
							// TODO : YOUR CODE HERE FOR LEFT ACTION
							Toast.makeText(activity, "it work on left",
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSwipeRight(ListView listView,
								int[] reverseSortedPositions) {
							// Log.i(ProfileMenuActivity.class.getClass().getName(),
							// "swipe right : pos="+reverseSortedPositions[0]);
							// TODO : YOUR CODE HERE FOR RIGHT ACTION
							Toast.makeText(activity, "it work on right",
									Toast.LENGTH_SHORT).show();
						}
					}, false, // example : left action =without dismiss
					false); // example : right action without dismiss animation
			getListView().setOnTouchListener(touchListener);
			// Setting this scroll listener is required to ensure that during
			// ListView scrolling,
			// we don't look for swipes.
			getListView().setOnScrollListener(
					touchListener.makeScrollListener());
		}

		@Override
		public void onDestroy() {

			Singleton_TabInfoHolder tabInfoHolder = Singleton_TabInfoHolder
					.getInstance();

			myListView.getLayoutParams().height = height;
			myListView.getLayoutParams().width = width;
			myListView.requestLayout();

			fragView.getLayoutParams().height = 0;
			fragView.getLayoutParams().width = 0;

			tabInfoHolder.setPageSwipeable(true);
			tabInfoHolder.setTabClickable(true);

			super.onDestroy();

		}
		
		@Override
		public void onListItemClick(ListView list, View view, int position, long id) {
			super.onListItemClick(list, view, position, id);
			MediaViewHolder holder = (MediaViewHolder) view.getTag();
			Singleton_PlayerInfoHolder.getInstance().currentFile = (String) holder.path;
			activity.startPlay(Singleton_PlayerInfoHolder.getInstance().currentFile);
			
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainMusicPlayerActivity) activity;
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

		View view = (LinearLayout) inflater.inflate(
				R.layout.lf_sortbyalbum_layout, container, false);

		cursor = view
				.getContext()
				.getContentResolver()
				.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null,
						null, null, MediaStore.Audio.Albums.ALBUM_KEY);

		if (null != cursor) {
			cursor.moveToFirst();

			albumsAdapter = new AlbumsCursorAdapter(activity,
					view.getContext(), R.layout.musicplayer_listitem, cursor);

			setListAdapter(albumsAdapter);

		}

		fragView = (View) view.findViewById(R.id.songs_of_an_album_frame);
		myListView = (View) view.findViewById(R.id.albumfrag_list);

		return view;
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);

		AlbumsViewHolder holder= (AlbumsViewHolder)view.getTag();
		albumTitle=holder.albumTitle.getText().toString();

		// Create new fragment and transaction
		transaction = getFragmentManager().beginTransaction();
		childFragment = new ListFragment_SortByAlbumChild();

		// Replace whatever is in
		// the fragment_container view with this fragment, // and add the
		// transaction to the back stack
		height = myListView.getHeight();
		width = myListView.getWidth();

		myListView.getLayoutParams().height = 0;
		myListView.getLayoutParams().width = 0;
		myListView.requestLayout();

		if (fragView != null) {

			fragView.getLayoutParams().height = height;
			fragView.getLayoutParams().width = width;
			fragView.requestLayout();
		}
		tabInfoHolder.setPageSwipeable(false);
		tabInfoHolder.setTabClickable(false);

		transaction.replace(R.id.songs_of_an_album_frame, childFragment);
		// Commit the transaction
		transaction.addToBackStack(null);
		transaction.commit();
	}

}
