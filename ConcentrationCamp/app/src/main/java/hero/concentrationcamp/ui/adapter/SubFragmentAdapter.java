package hero.concentrationcamp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import hero.concentrationcamp.mvp.model.entity.SourceColumn;
import hero.concentrationcamp.ui.base.BaseFragment;

/**
 * Created by hero on 2016/12/2 0002.
 */

public class SubFragmentAdapter extends FragmentStatePagerAdapter {
    private List<BaseFragment> mFragments;
    private SourceColumn[]  mColumns;
    public SubFragmentAdapter(FragmentManager fm, List<BaseFragment> fragments, SourceColumn[] columns) {
        super(fm);
        mFragments = fragments;
        mColumns = columns;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mColumns[position].getName();
    }
}
