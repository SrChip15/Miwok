package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhrasesFragment extends Fragment {

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


	public PhrasesFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.word_list, container, false);

		// Create AudioManager to request audio focus
		mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

		// Add ArrayList labelled phrases containing strings
		final ArrayList<Word> phrases = new ArrayList<Word>();
		phrases.add(new Word("Where are you going?", "minto wuksus",
				R.raw.phrase_where_are_you_going));
		phrases.add(new Word("What is your name?", "tinnә oyaase'nә",
				R.raw.phrase_what_is_your_name));
		phrases.add(new Word("My name is...", "oyaaset...", R.raw.phrase_my_name_is));
		phrases.add(new Word("How are you feeling?", "michәksәs?",
				R.raw.phrase_how_are_you_feeling));
		phrases.add(new Word("I’m feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good));
		phrases.add(new Word("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
		phrases.add(new Word("Yes, I’m coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
		phrases.add(new Word("I’m coming.", "әәnәm", R.raw.phrase_im_coming));
		phrases.add(new Word("Let’s go.", "yoowutis", R.raw.phrase_lets_go));
		phrases.add(new Word("Come here.", "әnni'nem", R.raw.phrase_come_here));

		// Use Adapter to display
		WordAdapter wordAdapter = new WordAdapter(getActivity(), phrases, R.color.category_phrases);

		// Get the ListView object from the word_list layout
		ListView listView = (ListView) rootView.findViewById(R.id.list_view);

		// Attach WordAdapter from above to the ListView to display each word in the list
		listView.setAdapter(wordAdapter);

		// Register click listener to play audio when list item is clicked
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				// Release and nullify media player to rid priors
				releaseMediaPlayer();

				// Get clicked word via position
				Word phrase = phrases.get(position);

				// Request audio focus for playback
				int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
						AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

				// Check for audio focus
				if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
					// We have audio focus
					// Initialize media player to the audio file from the clicked word
					mMediaPlayer = MediaPlayer.create(getActivity(),
							phrase.getAudioResourceId());

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

		return rootView;
	}

	/**
	 * Called when the Fragment is no longer started.  This is generally
	 * tied to {@link Activity#onStop() Activity.onStop} of the containing
	 * Activity's lifecycle.
	 */
	@Override
	public void onStop() {
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
