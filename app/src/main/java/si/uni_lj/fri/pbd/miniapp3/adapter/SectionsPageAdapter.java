package si.uni_lj.fri.pbd.miniapp3.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import si.uni_lj.fri.pbd.miniapp3.ui.favorites.FavoritesFragment;
import si.uni_lj.fri.pbd.miniapp3.ui.search.SearchFragment;

public class SectionsPageAdapter extends FragmentStateAdapter {
    public int mCount;

    public SectionsPageAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        switch (index)
        {
            case 0:
                return new SearchFragment();
            case 1:
                return new FavoritesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    public int getRealPosition(int position){
        return position % mCount;
    }

}
