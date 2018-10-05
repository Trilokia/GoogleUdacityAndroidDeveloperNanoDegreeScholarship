package inc.trilokia.popularmovies_2.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class MovieListOnScrollListener extends RecyclerView.OnScrollListener {

    //Class Constants
    private static final int VISIBLE_THRESHOLD = 6;

    //Class Variables
    private GridLayoutManager mLayoutManager;
    private boolean mLoading = false;

    public void setLoading(boolean loading) {
        mLoading = loading;
    }

    public MovieListOnScrollListener(GridLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int itemCount = mLayoutManager.getItemCount();
        int lastItemPosition = mLayoutManager.findLastVisibleItemPosition();

        if (lastItemPosition + VISIBLE_THRESHOLD >= itemCount && itemCount > 0 && !mLoading) {
            mLoading = true;
            loadMore();
        }
    }

    public abstract void loadMore();

}
