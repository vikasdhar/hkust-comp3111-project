package pacekeeper.musicplayerpackage;

import pacekeeper.musicplayerpackage.MediaCursorAdapter.MediaViewHolder;
import pacekeeper.musicplayerpackage.PlayListsCursorAdapter.PlayListsViewHolder;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListFragment_SortByPlayList extends ListFragment{
	private static MainMusicPlayerActivity activity;
	private Singleton_TabInfoHolder tabInfoHolder = Singleton_TabInfoHolder
			.getInstance();

	private static View fragView;
	private static View myListView;
	private static Cursor cursor;
	private PlayListsCursorAdapter playListsAdapter = null;

	private FragmentTransaction transaction;
	private Fragment childFragment;
	private static int height;
	private static int width;
	private static String playListName;
	private static Long playListID;
	
	public static class ListFragment_SortByPlayListChild extends ListFragment {
		private TextView playListIndicater;
		private ImageButton playAllButton;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View view = (LinearLayout) inflater.inflate(
					R.layout.lf_sortbyplaylistchild_layout, container, false);

			String whereValue[] = { playListName };

//			String[] projection = {
//                  MediaStore.Audio.Playlists.Members.AUDIO_ID,
//                  MediaStore.Audio.Playlists.Members.ARTIST,
//                  MediaStore.Audio.Playlists.Members.TITLE,
//                  MediaStore.Audio.Playlists.Members._ID
			
			Cursor cursor = view
					.getContext()
					.getContentResolver()
					.query(MediaStore.Audio.Playlists.Members.getContentUri("external",playListID ), null,
							MediaStore.Audio.Media.IS_MUSIC +" != 0 ", null,null);
			
			if (null != cursor) {
				cursor.moveToFirst();

				MediaCursorAdapter mediaAdapter = new MediaCursorAdapter(
						ListFragment_SortByPlayList.activity, view.getContext(),
						R.layout.musicplayer_listitem, cursor);

				setListAdapter(mediaAdapter);

			}
			
			playListIndicater=(TextView) view.findViewById(R.id.sortByPlayListChild_playListIndicator);
			playListIndicater.setText(playListName);
			playAllButton=(ImageButton) view.findViewById(R.id.sortByPlayListChild_playAll);
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
					false, // example : right action without dismiss animation
			        false,
			        false); 
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
		public void onListItemClick(ListView list, View view, int position,
				long id) {
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
				R.layout.lf_sortbyplaylist_layout, container, false);
		
		this.cursor = view
				.getContext()
				.getContentResolver()
				.query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, null,
						null, null, MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER);

		if (null != cursor) {
			cursor.moveToFirst();

			playListsAdapter = new PlayListsCursorAdapter(activity,
					view.getContext(), R.layout.musicplayer_listitem, cursor);

			setListAdapter(playListsAdapter);

		}
		fragView = (View) view.findViewById(R.id.songs_of_a_playlist_frame);
		myListView = (View) view.findViewById(R.id.playlistfrag_list);

		return view;
	}
	
	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);

		PlayListsViewHolder holder = (PlayListsViewHolder) view.getTag();
		playListName = holder.playListName.getText().toString();
		playListID=holder.listID;
		
		// Create new fragment and transaction
		transaction = getFragmentManager().beginTransaction();
		childFragment = new ListFragment_SortByPlayListChild();

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

		transaction.replace(R.id.songs_of_a_playlist_frame, childFragment);
		// Commit the transaction
		transaction.addToBackStack(null);
		transaction.commit();
	}
}
