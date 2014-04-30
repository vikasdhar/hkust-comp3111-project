package pacekeeper.musicplayerpackage;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class AssoMusicPlayerActivity extends Activity {

	private static final long UPDATE_FREQUENCY = 50;

	private Singleton_PlayerInfoHolder playerInfoHolder = Singleton_PlayerInfoHolder
			.getInstance();
	private ImageButton playButton = null;
	private ImageButton prevButton = null;
	private ImageButton nextButton = null;
	private ImageButton repeatButton = null;
	private ImageButton shuffleButton = null;
	private ImageView albumArtView = null;
	private int height;
	private int width;
	private View inflatedListView = null;

	private TextView textSongTitle = null;
	private TextView textArtist = null;
	private TextView textAlbum = null;
	private TextView textPrev = null;
	private TextView textNext = null;
	private SeekBar seekbar = null;

	private final Handler handler = new Handler();

	private final Runnable updatePositionRunnable2 = new Runnable() {
		public void run() {
			updatePosition2();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.assomusicplayeractivity_layout);

		textSongTitle = (TextView) findViewById(R.id.assoplayer_song_title);
		textArtist = (TextView) findViewById(R.id.assoplayer_artist);
		textAlbum = (TextView) findViewById(R.id.assoplayer_album);
		textPrev = (TextView) findViewById(R.id.assoplayer_prevSongDescription);
		textNext = (TextView) findViewById(R.id.assoplayer_nextSongDescription);
		seekbar = (SeekBar) findViewById(R.id.assoplayer_seekbar);
		playButton = (ImageButton) findViewById(R.id.assoplayer_play);
		prevButton = (ImageButton) findViewById(R.id.assoplayer_prev);
		nextButton = (ImageButton) findViewById(R.id.assoplayer_next);
		shuffleButton = (ImageButton) findViewById(R.id.assoplayer_shuffle);
		repeatButton = (ImageButton) findViewById(R.id.assoplayer_repeat);
		albumArtView = (ImageView) findViewById(R.id.assoplayer_showalbumart);
		inflatedListView = (View) findViewById(R.id.assoplayer_playlist);

		playButton.setOnClickListener(onButtonClick);
		nextButton.setOnClickListener(onButtonClick);
		prevButton.setOnClickListener(onButtonClick);
		repeatButton.setOnClickListener(onButtonClick);
		albumArtView.setOnClickListener(onButtonClick);
		shuffleButton.setOnClickListener(onButtonClick);
		albumArtView.setOnClickListener(onButtonClick);
		seekbar.setOnSeekBarChangeListener(seekBarChanged);
		resetStatic();
	}

	@Override
	protected void onResume() {
		super.onResume();
		playerInfoHolder.player.setOnCompletionListener(onCompletion);
		playerInfoHolder.player.setOnErrorListener(onError);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		handler.removeCallbacks(updatePositionRunnable2);
	}

	/**
	 * Reset the state base on MusicPlayer_with_songslist
	 * 
	 */
	protected void resetStatic() {
		if (!playerInfoHolder.isStarted) {
			albumArtView.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_expandplayer_placeholder));
		} else {
			updatePosition2();
			textSongTitle.setText(playerInfoHolder.songsList
					.getTitle(playerInfoHolder.currentFile));
			textArtist.setText(playerInfoHolder.songsList
					.getArtist(playerInfoHolder.currentFile));
			textAlbum.setText(playerInfoHolder.songsList
					.getAlbum(playerInfoHolder.currentFile));

			// album art part; to resize after knowing the actual image height
			ViewTreeObserver vto = albumArtView.getViewTreeObserver();
			vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
				public boolean onPreDraw() {

					int temp = albumArtView.getMeasuredWidth();
					albumArtView.getLayoutParams().height = temp;
					return true;
				}
			});
			playerInfoHolder.setAlbumArt(albumArtView,
					playerInfoHolder.currentFile, false);

			seekbar.setMax(playerInfoHolder.player.getDuration());
			if (playerInfoHolder.player.isPlaying()) {
				playButton.setImageResource(R.drawable.ic_action_pause);
			} else {
				playButton.setImageResource(R.drawable.ic_action_play);
			}
		}

		if (playerInfoHolder.isShuffle) {
			shuffleButton.setImageResource(R.drawable.ic_action_shuffle);
		} else {
			shuffleButton.setImageResource(R.drawable.ic_action_mute);
		}

		switch (playerInfoHolder.repeatMode) {

		// Repeat all
		case 2: {
			repeatButton.setImageResource(R.drawable.ic_action_repeat_a);
			break;
		}
		// Repeat once
		case 1: {
			repeatButton.setImageResource(R.drawable.ic_action_repeat_1);
			break;
		}
		// No repeat
		case 0: {
			repeatButton.setImageResource(R.drawable.ic_action_repeat_once);
			break;
		}
		}
	}

	protected void updatePosition2() {
		handler.removeCallbacks(updatePositionRunnable2);

		seekbar.setProgress(playerInfoHolder.player.getCurrentPosition());

		handler.postDelayed(updatePositionRunnable2, UPDATE_FREQUENCY);
	}

	public void startPlay(String file) {
		Log.i("Selected: ", file);

		playerInfoHolder.setAlbumArt(albumArtView, file, false);
		ViewTreeObserver vto = albumArtView.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {

				int temp = albumArtView.getMeasuredWidth();
				albumArtView.getLayoutParams().height = temp;
				return true;
			}
		});

		textSongTitle.setText(playerInfoHolder.songsList
				.getTitle(playerInfoHolder.currentFile));
		textArtist.setText(playerInfoHolder.songsList
				.getArtist(playerInfoHolder.currentFile));
		textAlbum.setText(playerInfoHolder.songsList
				.getAlbum(playerInfoHolder.currentFile));

		seekbar.setProgress(0);

		playerInfoHolder.player.stop();
		playerInfoHolder.player.reset();

		try {
			playerInfoHolder.player.setDataSource(file);
			playerInfoHolder.player.prepare();
			playerInfoHolder.player.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		seekbar.setMax(playerInfoHolder.player.getDuration());

		playButton.setImageResource(R.drawable.ic_action_pause);

		updatePosition2();

		playerInfoHolder.isStarted = true;
	}

	private void stopPlay() {
		playerInfoHolder.player.stop();
		playerInfoHolder.player.reset();

		textSongTitle.setText("No song selected");
		textArtist.setText(" ");
		textAlbum.setText(" ");

		playButton.setImageResource(R.drawable.ic_action_play);
		albumArtView.setImageDrawable(getResources().getDrawable(
				R.drawable.ic_expandplayer_placeholder));

		handler.removeCallbacks(updatePositionRunnable2);
		seekbar.setProgress(0);

		playerInfoHolder.isStarted = false;
	}

	private View.OnClickListener onButtonClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.assoplayer_play: {
				if (playerInfoHolder.player.isPlaying()) {

					handler.removeCallbacks(updatePositionRunnable2);
					playerInfoHolder.player.pause();
					playButton.setImageResource(R.drawable.ic_action_play);
				} else {
					if (playerInfoHolder.isStarted) {

						playerInfoHolder.player.start();
						playButton.setImageResource(R.drawable.ic_action_pause);
						updatePosition2();

					} else {
						if (playerInfoHolder.currentFile != null) {
							startPlay(playerInfoHolder.currentFile);
						} else {
							Toast.makeText((Activity) v.getContext(),
									"Please select a music!",
									Toast.LENGTH_SHORT).show();
						}
					}
				}

				break;
			}

			case R.id.assoplayer_prev: {

				if (playerInfoHolder.currentFile != null) {

					switch (playerInfoHolder.repeatMode) {

					// Repeat all
					case 2: {
						playerInfoHolder.currentFile = playerInfoHolder.currentList
								.prevFileLoop(playerInfoHolder.currentFile);
						startPlay(playerInfoHolder.currentFile);
						break;
					}
					// Repeat once
					case 1: {
						startPlay(playerInfoHolder.currentFile);
						break;
					}
					// No repeat
					case 0: {
						playerInfoHolder.currentFile = playerInfoHolder.currentList
								.prevFile(playerInfoHolder.currentFile);
						if (playerInfoHolder.currentFile == null) {
							Toast.makeText((Activity) v.getContext(),
									"This is the first song!",
									Toast.LENGTH_SHORT).show();
						} else
							startPlay(playerInfoHolder.currentFile);
						break;
					}
					}
				} else {
					stopPlay();
					Toast.makeText((Activity) v.getContext(),
							"Please select a music!", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			}

			case R.id.assoplayer_next: {

				if (playerInfoHolder.currentFile != null) {

					switch (playerInfoHolder.repeatMode) {

					// Repeat all
					case 2: {
						playerInfoHolder.currentFile = playerInfoHolder.currentList
								.nextFileLoop(playerInfoHolder.currentFile);
						startPlay(playerInfoHolder.currentFile);
						break;
					}
					// Repeat once
					case 1: {
						startPlay(playerInfoHolder.currentFile);
						break;
					}
					// No repeat
					case 0: {
						playerInfoHolder.currentFile = playerInfoHolder.currentList
								.nextFile(playerInfoHolder.currentFile);
						if (playerInfoHolder.currentFile == null) {
							Toast.makeText((Activity) v.getContext(),
									"This is the last song!",
									Toast.LENGTH_SHORT).show();
						} else
							startPlay(playerInfoHolder.currentFile);
						break;
					}
					}
				} else {
					stopPlay();
					Toast.makeText((Activity) v.getContext(),
							"Please select a music!", Toast.LENGTH_SHORT)
							.show();
				}

				break;
			}
			case R.id.assoplayer_repeat: {
				switch (playerInfoHolder.repeatMode) {
				case 2: {
					repeatButton
							.setImageResource(R.drawable.ic_action_repeat_1);
					playerInfoHolder.repeatMode = 1;
					break;
				}
				case 1: {
					repeatButton
							.setImageResource(R.drawable.ic_action_repeat_once);
					playerInfoHolder.repeatMode = 0;
					break;
				}
				case 0: {
					repeatButton
							.setImageResource(R.drawable.ic_action_repeat_a);
					playerInfoHolder.repeatMode = 2;
					break;
				}

				}
				break;
			}
			case R.id.assoplayer_shuffle: {
				if (playerInfoHolder.isShuffle) {
					playerInfoHolder.isShuffle = false;
					shuffleButton.setImageResource(R.drawable.ic_action_mute);
					playerInfoHolder.currentList.unrandomize();
				} else {
					playerInfoHolder.isShuffle = true;
					shuffleButton
							.setImageResource(R.drawable.ic_action_shuffle);
					playerInfoHolder.currentList.randomize();
				}
				break;
			}
			case R.id.assoplayer_showalbumart: {

				height = albumArtView.getHeight();
				width = albumArtView.getWidth();

				albumArtView.getLayoutParams().height = 0;
				albumArtView.getLayoutParams().width = 0;
				albumArtView.requestLayout();

				inflatedListView.getLayoutParams().height = height;
				inflatedListView.getLayoutParams().width = width;
				inflatedListView.requestLayout();
				break;
			}
			}
		}
	};
	private SeekBar.OnSeekBarChangeListener seekBarChanged = new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			playerInfoHolder.isMoveingSeekBar = false;
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			playerInfoHolder.isMoveingSeekBar = true;
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (playerInfoHolder.isMoveingSeekBar) {
				playerInfoHolder.player.seekTo(progress);

				Log.i("OnSeekBarChangeListener", "onProgressChanged");
			}
		}
	};

	private MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			stopPlay();
			switch (playerInfoHolder.repeatMode) {

			// Repeat all
			case 2: {
				playerInfoHolder.currentFile = playerInfoHolder.currentList
						.nextFileLoop(playerInfoHolder.currentFile);

				startPlay(playerInfoHolder.currentFile);
				break;
			}
			// Repeat once
			case 1: {
				startPlay(playerInfoHolder.currentFile);
				break;
			}
			// No repeat
			case 0: {
				playerInfoHolder.currentFile = playerInfoHolder.currentList
						.nextFile(playerInfoHolder.currentFile);
				if (playerInfoHolder.currentFile == null) {

				} else
					startPlay(playerInfoHolder.currentFile);
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
}
