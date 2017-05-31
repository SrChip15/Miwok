package com.example.android.miwok;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CategoryAdapter extends FragmentPagerAdapter {

	public CategoryAdapter(FragmentManager fm) {
		super(fm);
	}

	/**
	 * Return the Fragment associated with a specified position.
	 *
	 * @param position
	 */
	@Override
	public Fragment getItem(int position) {
		if (position == 0) {
			return new NumbersFragment();
		} else if (position == 1) {
			return new FamilyFragment();
		} else if (position == 2) {
			return new PhrasesFragment();
		} else {
			return new ColorsFragment();
		}
	}

	/**
	 * Return the number of views available.
	 */
	@Override
	public int getCount() {
		return 4;
	}
}
