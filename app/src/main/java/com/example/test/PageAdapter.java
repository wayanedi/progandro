package com.example.test;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private int mNoOfTab;


    public PageAdapter(FragmentManager fm, int numberOfTabs){

        super(fm);
        this.mNoOfTab = numberOfTabs;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:{
                Tab1 tab1 = new Tab1();
                return tab1;
            }

            case 1:{
                Tab2 tab2 = new Tab2();
                return tab2;
            }

            case 2:{
                Tab3 tab3 = new Tab3();
                return tab3;
            }

            default:
            {
                return null;
            }
        }
    }

    @Override
    public int getCount() {
        return mNoOfTab;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(position==1){
            String title = "FIREBASE ";
            return title.subSequence(title.lastIndexOf(".") + 1, title.length());
        }else {
            String title = "tab " + (position + 1);
            return title.subSequence(title.lastIndexOf(".") + 1, title.length());
        }
    }

}
