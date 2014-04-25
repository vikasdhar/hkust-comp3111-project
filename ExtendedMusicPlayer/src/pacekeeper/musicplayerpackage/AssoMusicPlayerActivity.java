package pacekeeper.musicplayerpackage;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
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
	private ImageView showAlbumArtButton = null;
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
		showAlbumArtButton = (ImageView) findViewById(R.id.assoplayer_showalbumart);

		playButton.setOnClickListener(onButtonClick);
		nextButton.setOnClickListener(onButtonClick);
		prevButton.setOnClickListener(onButtonClick);
		repeatButton.setOnClickListener(onButtonClick);
		showAlbumArtButton.setOnClickListener(onButtonClick);
		seekbar.setOnSeekBarChangeListener(seekBarChanged);

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
		// Intent intename = getIntent();
		// String uname = (String) intename.getSerializableExtra("USERNAME");
		// textSongTitle.setText(uname);

		resetStatic();
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
			showAlbumArtButton.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_expandplayer_placeholder));
		} else {
			updatePosition2();
			textSongTitle.setText(playerInfoHolder.songsList
					.getTitle(playerInfoHolder.currentFile));
			textArtist.setText(playerInfoHolder.songsList
					.getArtist(playerInfoHolder.currentFile));
			textAlbum.setText(playerInfoHolder.songsList
					.getAlbum(playerInfoHolder.currentFile));
			playerInfoHolder.setAlbumArt(showAlbumArtButton,
					playerInfoHolder.currentFile, false);

			// album art part; to resize after knowing the actual image height

			ViewTreeObserver vto = showAlbumArtButton.getViewTreeObserver();
			vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
				public boolean onPreDraw() {

					int temp = showAlbumArtButton.getMeasuredWidth();
					showAlbumArtButton.getLayoutParams().height = temp;
					return true;
				}
			});

			seekbar.setMax(playerInfoHolder.player.getDuration());
			if (playerInfoHolder.player.isPlaying()) {
				playButton.setImageResource(R.drawable.ic_action_pause);
			} else {
				playButton.setImageResource(R.drawable.ic_action_play);
			}
		}
	}

	protected void updatePosition2() {
		handler.removeCallbacks(updatePositionRunnable2);

		seekbar.setProgress(playerInfoHolder.player.getCurrentPosition());

		handler.postDelayed(updatePositionRunnable2, UPDATE_FREQUENCY);
	}

	void startPlay(String file) {
		Log.i("Selected: ", file);

		playerInfoHolder.setAlbumArt(showAlbumArtButton, file, false);
		ViewTreeObserver vto = showAlbumArtButton.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {

				int temp = showAlbumArtButton.getMeasuredWidth();
				showAlbumArtButton.getLayoutParams().height = temp;
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
		playButton.setImageResource(R.drawable.ic_action_play);
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
}
