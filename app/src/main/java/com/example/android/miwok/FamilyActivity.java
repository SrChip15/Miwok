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

public class FamilyActivity extends AppCompatActivity {

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

		// Add ArrayList labelled familyMembers containing strings
		final ArrayList<Word> familyMembers = new ArrayList<Word>();
		familyMembers.add(new Word("father", "әpә", R.drawable.family_father, R.raw.family_father));
		familyMembers.add(new Word("mother", "әṭa", R.drawable.family_mother, R.raw.family_mother));
		familyMembers.add(new Word("son", "angsi", R.drawable.family_son, R.raw.family_son));
		familyMembers.add(new Word("daughter", "tune", R.drawable.family_daughter,
				R.raw.family_daughter));
		familyMembers.add(new Word("older brother", "taachi", R.drawable.family_older_brother,
				R.raw.family_older_brother));
		familyMembers.add(new Word("younger brother", "chalitti", R.drawable.family_younger_brother,
				R.raw.family_younger_brother));
		familyMembers.add(new Word("older sister", "teṭe", R.drawable.family_older_sister,
				R.raw.family_older_sister));
		familyMembers.add(new Word("younger sister", "kolliti", R.drawable.family_younger_sister,
				R.raw.family_younger_sister));
		familyMembers.add(new Word("grandmother", "ama", R.drawable.family_grandmother,
				R.raw.family_grandmother));
		familyMembers.add(new Word("grandfather", "paapa", R.drawable.family_grandfather,
				R.raw.family_grandfather));

		// Use Adapter to display
		WordAdapter wordAdapter = new WordAdapter(this, familyMembers,
				R.color.category_family);

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
				Word familyMember = familyMembers.get(position);

				// Request audio focus for playback
				int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
						AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

				// Check for audio focus
				if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
					// We have audio focus
					// Initialize media player to the audio file from the clicked word
					mMediaPlayer = MediaPlayer.create(FamilyActivity.this,
							familyMember.getAudioResourceId());

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
		// If the media player is not null, then it is currently playing an audio file
		if (mMediaPlayer != null) {
			// Release resources in whatever state
			mMediaPlayer.release();

			// De-configure media player
			mMediaPlayer = null;

			// Abandon audio focus in any audio focus state
			// and unregister audio focus change listener
			mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
		}
	}
}