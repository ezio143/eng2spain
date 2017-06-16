package com.example.dell.myapplication;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by DELL on 24-02-2017.
 */

public class CategoryAdapter extends FragmentPagerAdapter {
    private Context mContext;


    public CategoryAdapter(Context context,FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return  new NumbersFragment();
            case 1:return new FamilyFragment();
            case 2:return new ColorsFragment();
            default:return new PhrasesFragment();
        }
    }

    @Override
    public int getCount() {
        //we need 4 pages in the viewpager
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:return mContext.getString(R.string.category_numbers);
            case 1:return mContext.getString(R.string.category_family);
            case 2:return mContext.getString(R.string.category_colors);
            case 3:return mContext.getString(R.string.category_phrases);
            default: throw new IllegalArgumentException("tab");
        }
    }
}
