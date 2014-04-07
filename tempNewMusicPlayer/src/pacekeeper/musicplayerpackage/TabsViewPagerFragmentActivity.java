package pacekeeper.musicplayerpackage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import pacekeeper.musicplayerpackage.R;

import android.support.v4.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabContentFactory;



/**
 * The <code>TabsViewPagerFragmentActivity</code> class implements the Fragment
 * activity that maintains a TabHost using a ViewPager.
 */
public class TabsViewPagerFragmentActivity extends FragmentActivity implements
		TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

	private TabHost mTabHost;
	private ViewPager mViewPager;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabsViewPagerFragmentActivity.TabInfo>();
	private PagerAdapter mPagerAdapter;

	/**
	 * Maintains extrinsic info of a tab's construct
	 */
	private class TabInfo {
		private String tag;
		private Class<?> clss;
		private Bundle args;
		private ListFragment fragment;

		TabInfo(String tag, Class<?> clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}

	}

	/**
	 * A simple factory that returns dummy views to the Tabhost
	 */
	class TabFactory implements TabContentFactory {

		private final Context mContext;

		/**
		 * @param context
		 */
		public TabFactory(Context context) {
			mContext = context;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
		 */
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}

	}

	// Music part starts
	private static final int UPDATE_FREQUENCY = 50;
	private static final int STEP_VALUE = 4000;

	private MediaCursorAdapter mediaAdapter = null;
	private TextView selectedFile = null;
	private SeekBar seekbar = null;
	private MediaPlayer player = null;
	private ImageButton playButton = null;
	private ImageButton prevButton = null;
	private ImageButton nextButton = null;
	private ImageView showalbumartButton = null;

	private boolean isStarted = true;
	static String currentFile = "";
	private boolean isMoveingSeekBar = false;

	public SongsList songsList_sortedby_TITLE;
	public SongsList songsList_sortedby_ARTIST;
	public SongsList songsList_sortedby_ALBUM;

	private final Handler handler = new Handler();

	private final Runnable updatePositionRunnable = new Runnable() {
		public void run() {
			updatePosition();
		}
	};

	void startPlay(String file) {
		Log.i("Selected: ", file);
		int listPosition = songsList_sortedby_TITLE.matchWithName(currentFile);
		
		
//		MediaMetadataRetriever md = new MediaMetadataRetriever();
//		md.setDataSource(file);
//
//		byte[] art = md.getEmbeddedPicture();
//		if (art != null) {
//			InputStream is = new ByteArrayInputStream(md.getEmbeddedPicture());
//			Bitmap bm = BitmapFactory.decodeStream(is);
//			showalbumartButton.setImageBitmap(bm);
//		} else {
//			showalbumartButton.setImageDrawable(getResources().getDrawable(
//					android.R.drawable.ic_dialog_dialer));
//		}
		
		selectedFile.setText(songsList_sortedby_TITLE.getTitle(listPosition)
				+ "-" + songsList_sortedby_TITLE.getArtist(listPosition));

		// selectedFile
		// .setText(md
		// .extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
		// + "-"
		// + md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
		seekbar.setProgress(0);

		player.stop();
		player.reset();

		try {
			player.setDataSource(file);
			player.prepare();
			player.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		seekbar.setMax(player.getDuration());

		playButton.setImageResource(R.drawable.ic_action_pause);

		updatePosition();

		isStarted = true;
	}

	private void stopPlay() {
		player.stop();
		player.reset();
		playButton.setImageResource(android.R.drawable.ic_media_play);
		handler.removeCallbacks(updatePositionRunnable);
		seekbar.setProgress(0);

		isStarted = false;
	}

	private void updatePosition() {
		handler.removeCallbacks(updatePositionRunnable);

		seekbar.setProgress(player.getCurrentPosition());

		handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		handler.removeCallbacks(updatePositionRunnable);
		player.stop();
		player.reset();
		player.release();

		player = null;
	}

	private MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			stopPlay();
		}
	};
	private View.OnClickListener onButtonClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.play: {
				if (player.isPlaying()) {
					handler.removeCallbacks(updatePositionRunnable);
					player.pause();
					playButton
							.setImageResource(R.drawable.ic_action_play);
				} else {
					if (isStarted) {
						player.start();
						playButton
								.setImageResource(R.drawable.ic_action_pause);

						updatePosition();
					} else {
						startPlay(currentFile);
					}
				}

				break;
			}
			case R.id.next: {
				// int seekto = player.getCurrentPosition() + STEP_VALUE;
				//
				// if (seekto > player.getDuration())
				// seekto = player.getDuration();

				// player.pause();
				// player.seekTo(seekto);
				// player.start();
				
			
				
				
				currentFile = songsList_sortedby_TITLE
						.getPath(songsList_sortedby_TITLE
								.matchWithName(currentFile) + 1);
				startPlay(currentFile);
				break;
			}
			case R.id.prev: {
				// int seekto = player.getCurrentPosition() - STEP_VALUE;
				//
				// if (seekto < 0)
				// seekto = 0;
				//
				// player.pause();
				// player.seekTo(seekto);
				// player.start();
				
				
				currentFile = songsList_sortedby_TITLE
						.getPath(songsList_sortedby_TITLE
								.matchWithName(currentFile) - 1);
				startPlay(currentFile);
				break;
			}
			case R.id.showalbumart: {

				// Intent intObj = new Intent(PlayAudioExample.this,
				// GreetingActivity.class);
				// intObj.putExtra("USERNAME", "this is a test");
				// startActivity(intObj);
				break;
			}
			}
		}
	};
	private MediaPlayer.OnErrorListener onError = new MediaPlayer.OnErrorListener() {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			// returning false will call the OnCompletionListener
			return false;
		}
	};

	private SeekBar.OnSeekBarChangeListener seekBarChanged = new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			isMoveingSeekBar = false;
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			isMoveingSeekBar = true;
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (isMoveingSeekBar) {
				player.seekTo(progress);

				Log.i("OnSeekBarChangeListener", "onProgressChanged");
			}
		}
	};

	// music part methods end

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Inflate the layout
		setContentView(R.layout.tabs_viewpager_layout);
		// Initialise the TabHost
		this.initialiseTabHost(savedInstanceState);
		this.intialiseViewPager();

		// Music part
		selectedFile = (TextView) findViewById(R.id.selectedfile);
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		playButton = (ImageButton) findViewById(R.id.play);
		prevButton = (ImageButton) findViewById(R.id.prev);
		nextButton = (ImageButton) findViewById(R.id.next);
		showalbumartButton = (ImageView) findViewById(R.id.showalbumart);

		player = new MediaPlayer();

		player.setOnCompletionListener(onCompletion);
		player.setOnErrorListener(onError);
		seekbar.setOnSeekBarChangeListener(seekBarChanged);

		playButton.setOnClickListener(onButtonClick);
		nextButton.setOnClickListener(onButtonClick);
		prevButton.setOnClickListener(onButtonClick);
		showalbumartButton.setOnClickListener(onButtonClick);
		showalbumartButton.setImageDrawable(getResources().getDrawable(
				android.R.drawable.ic_dialog_dialer));

		songsList_sortedby_TITLE = new SongsList(this,
				MediaStore.Audio.Media.TITLE_KEY);
		songsList_sortedby_ARTIST = new SongsList(this,
				MediaStore.Audio.Media.ARTIST_KEY);
		songsList_sortedby_ALBUM = new SongsList(this,
				MediaStore.Audio.Media.ARTIST_KEY);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("tab", mTabHost.getCurrentTabTag()); // save the tab
																// selected
		super.onSaveInstanceState(outState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.mTabHost.setCurrentTabByTag(savedInstanceState
					.getString("tab")); // set the tab as per the saved state
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Initialise ViewPager
	 */
	private void intialiseViewPager() {

		List<ListFragment> fragments = new Vector<ListFragment>();
		fragments.add((ListFragment) ListFragment.instantiate(this,
				Tab1Fragment.class.getName()));
		fragments.add((ListFragment) ListFragment.instantiate(this,
				Tab2Fragment.class.getName()));
		fragments.add((ListFragment) ListFragment.instantiate(this,
				Tab3Fragment.class.getName()));
		this.mPagerAdapter = new PagerAdapter(
				super.getSupportFragmentManager(), fragments);

		//
		this.mViewPager = (ViewPager) super.findViewById(R.id.tabviewpager);
		this.mViewPager.setAdapter(this.mPagerAdapter);
		this.mViewPager.setOnPageChangeListener(this);
	}

	/**
	 * Initialise the Tab Host
	 */
	private void initialiseTabHost(Bundle args) {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;
		TabsViewPagerFragmentActivity.AddTab(this, this.mTabHost, this.mTabHost
				.newTabSpec("Tab1").setIndicator("Tab 1"),
				(tabInfo = new TabInfo("Tab1", Tab1Fragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		TabsViewPagerFragmentActivity.AddTab(this, this.mTabHost, this.mTabHost
				.newTabSpec("Tab2").setIndicator("Tab 2"),
				(tabInfo = new TabInfo("Tab2", Tab2Fragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		TabsViewPagerFragmentActivity.AddTab(this, this.mTabHost, this.mTabHost
				.newTabSpec("Tab3").setIndicator("Tab 3"),
				(tabInfo = new TabInfo("Tab3", Tab3Fragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		// Default to first tab
		// this.onTabChanged("Tab1");
		//
		mTabHost.setOnTabChangedListener(this);
	}

	/**
	 * Add Tab content to the Tabhost
	 * 
	 * @param activity
	 * @param tabHost
	 * @param tabSpec
	 * @param clss
	 * @param args
	 */
	private static void AddTab(TabsViewPagerFragmentActivity activity,
			TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(activity.new TabFactory(activity));
		tabHost.addTab(tabSpec);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
	 */
	public void onTabChanged(String tag) {
		// TabInfo newTab = this.mapTabInfo.get(tag);
		int pos = this.mTabHost.getCurrentTab();
		this.mViewPager.setCurrentItem(pos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled
	 * (int, float, int)
	 */
	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected
	 * (int)
	 */
	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		this.mTabHost.setCurrentTab(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.ViewPager.OnPageChangeListener#
	 * onPageScrollStateChanged(int)
	 */
	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}
}
