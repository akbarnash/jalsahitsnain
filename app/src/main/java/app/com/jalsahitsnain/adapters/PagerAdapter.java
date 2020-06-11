package app.com.jalsahitsnain.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import app.com.jalsahitsnain.activity.news.NewsFragment;
import app.com.jalsahitsnain.fragments.AudiosFragment;
import app.com.jalsahitsnain.fragments.IslamiFragment;
import app.com.jalsahitsnain.fragments.VideoFragment;

/**
 * Created by mdmunirhossain on 12/18/17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                NewsFragment tab1 = new NewsFragment();
                return tab1;
            case 1:
                VideoFragment tab2 = new VideoFragment();
                return tab2;
            case 2:
                AudiosFragment tab3 = new AudiosFragment();
                return tab3;
            case 3:
                IslamiFragment tab4 = new IslamiFragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
