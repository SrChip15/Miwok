package com.example.android.miwok;

class Word {

    /**
     * Default translation for the word
     */
    private String mDefaultTranslation;

    /**
     * Miwok translation for the word
     */
    private String mMiwokTranslation;

    /**
     * Image resource ID for the image associated with the word
     */
    private int mImageResourceId;

    /**
     * Audio resource ID for the audio recording of the miwok word pronounciation
     */
    private int mAudioResourceId;

    /**
     * Create a new Word object
     *
     * @param miwokTranslation   a String for the word in Miwok language
     * @param defaultTranslation a String for the word in the language that the user understands
     * @param audioResourceId a int for the resource ID of the audio file associated with this item
     */
    Word(String defaultTranslation, String miwokTranslation, int audioResourceId) {
        this(defaultTranslation, miwokTranslation, 0, audioResourceId);
    }

    /**
     * Create a new Word object
     *
     * @param defaultTranslation a String for the word in the language that the user understands
     * @param miwokTranslation   a String for the word in Miwok language
     * @param imageResourceId    an int for the drawable resource ID for the image associated
     *                           with the word object
     */
    Word(String defaultTranslation, String miwokTranslation, int imageResourceId,
         int audioResourceId) {
        this.mDefaultTranslation = defaultTranslation;
        this.mMiwokTranslation = miwokTranslation;
        this.mImageResourceId = imageResourceId;
        this.mAudioResourceId = audioResourceId;
    }

    /**
     * Get the default translation of the word
     *
     * @return a String for the default translation
     */
    String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    /**
     * Get the miwok translation of the word
     *
     * @return a String for the miwok translation
     */
    String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    /**
     * Get image file's resource ID
     *
     * @return an int value for the resource ID that is generated by the R class
     */
    int getImageResourceId() {
        return mImageResourceId;
    }

    /**
     * Get audio file's resource ID
     *
     * @return an int value for the resource ID that is generated by the R class
     */
    public int getAudioResourceId() {
        return mAudioResourceId;
    }

    @Override
    public String toString() {
        return "Word{" +
                "mDefaultTranslation='" + mDefaultTranslation + '\'' +
                ", mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", mImageResourceId=" + mImageResourceId +
                ", mAudioResourceId=" + mAudioResourceId +
                '}';
    }
}
