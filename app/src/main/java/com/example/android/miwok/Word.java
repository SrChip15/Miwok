package com.example.android.miwok;

public class Word {

    /**
     * Default translation for the word
     */
    private String mDefaultTranslation;

    /**
     * Miwok translation for the word
     */
    private String mMiwokTranslation;

    /**
     * Create a new Word object
     *
     * @param miwokTranslation   a String for the word in Miwok language
     * @param defaultTranslation a String for the word in the language that the user understands
     */
    public Word(String miwokTranslation, String defaultTranslation) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTranslation;
    }

    /**
     * Get the default translation of the word
     *
     * @return a String for the default translation
     */
    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    /**
     * Get the miwok translation of the word
     *
     * @return a String for the miwok translation
     */
    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    @Override
    public String toString() {
        return "" + getMiwokTranslation() + "\n" + getDefaultTranslation();
    }
}
