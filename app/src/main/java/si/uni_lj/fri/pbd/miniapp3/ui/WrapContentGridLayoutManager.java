package si.uni_lj.fri.pbd.miniapp3.ui;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WrapContentGridLayoutManager extends GridLayoutManager {

    public WrapContentGridLayoutManager(Context context, int cnt) {
        super(context, cnt);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("Error", "IndexOutOfBoundsException in RecyclerView happens");
        }
    }
}
