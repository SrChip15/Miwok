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


public class NumbersActivity extends AppCompatActivity {

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

		// Add ArrayList labelled words containing strings
		final ArrayList<Word> words = new ArrayList<Word>();
		words.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
		words.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
		words.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
		words.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
		words.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
		words.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
		words.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
		words.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
		words.add(new Word("nine", "wo’e", R.drawable.number_nine, R.raw.number_nine));
		words.add(new Word("ten", "na’aacha", R.drawable.number_ten, R.raw.number_ten));

		// Use Adapter to display
		WordAdapter wordAdapter = new WordAdapter(NumbersActivity.this, words,
				R.color.category_numbers);

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
				Word word = words.get(position);

				// Request audio focus for playback
				int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
						AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

				// Check for audio focus
				if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
					// We have audio focus
					// Initialize media player to the audio file from the clicked word
					mMediaPlayer = MediaPlayer.create(NumbersActivity.this,
							word.getAudioResourceId());

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
