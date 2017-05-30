package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

	/**
	 * Handle media playback
	 */
	private MediaPlayer mMediaPlayer;

	/**
	 * Manage audio focus while playing an audio file
	 */
	private AudioManager mAudioManager;

	/**
	 * Setup listener to listen for audio focus changes
	 * and manage audio playback appropriately
	 */
	private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new
			AudioManager.OnAudioFocusChangeListener() {
				@Override
				public void onAudioFocusChange(int focusChange) {
					switch (focusChange) {
						case AudioManager.AUDIOFOCUS_GAIN:
							mMediaPlayer.start();
							break;
						case AudioManager.AUDIOFOCUS_LOSS:
							mMediaPlayer.stop();
							releaseMediaPlayer();
							break;
						case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
							mMediaPlayer.pause();
							mMediaPlayer.seekTo(0);
							break;
						case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
							mMediaPlayer.pause();
							mMediaPlayer.seekTo(0);
							break;
					}
				}
			};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_list);

		// Create AudioManager to request audio focus
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		// Add ArrayList labelled colors containing strings
		final ArrayList<Word> colors = new ArrayList<Word>();
		colors.add(new Word("red", "weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
		colors.add(new Word("green", "chokokki", R.drawable.color_green, R.raw.color_green));
		colors.add(new Word("brown", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
		colors.add(new Word("gray", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
		colors.add(new Word("black", "kululli", R.drawable.color_black, R.raw.color_black));
		colors.add(new Word("white", "kelelli", R.drawable.color_white, R.raw.color_white));
		colors.add(new Word("dusty yellow", "ṭopiisә", R.drawable.color_dusty_yellow,
				R.raw.color_dusty_yellow));
		colors.add(new Word("mustard yellow", "chiwiiṭә", R.drawable.color_mustard_yellow,
				R.raw.color_mustard_yellow));

		// Use Adapter to display
		WordAdapter wordAdapter = new WordAdapter(this, colors, R.color.category_colors);

		// Get the ListView object from the word_list layout
		ListView listView = (ListView) findViewById(R.id.list_view);

		// Attach WordAdapter from above to the ListView to display each word in the list
		listView.setAdapter(wordAdapter);

		// Register click listener to play audio when list item is clicked
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				// Release and nullify media player to rid priors
				releaseMediaPlayer();

				// Get clicked word via position
				Word color = colors.get(position);

				// Request audio focus for playback
				int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
						AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

				// Check for audio focus
				if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
					// We have audio focus
					// Initialize media player to the audio file from the clicked word
					mMediaPlayer = MediaPlayer.create(ColorsActivity.this, color.getAudioResourceId());

					// Start audio playback
					mMediaPlayer.start();

					// Register listener to check for audio playback completion
					mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mediaPlayer) {
							// Release resources and de-configure media player
							// on audio playback completion
							releaseMediaPlayer();
						}
					});
				}
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
		// When the activity is stopped, release the media player resources since we will not be
		// playing any more sounds
		releaseMediaPlayer();
	}

	/**
	 * Clean up media player by releasing it's resources
	 */
	private void releaseMediaPlayer() {
		if (mMediaPlayer != null) {
			// Release resources in whatever state
			mMediaPlayer.release();

			// De configure media player
			mMediaPlayer = null;

			// Abandon audio focus in any audio focus state
			// and unregister audio focus change listener
			mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
		}
	}
}