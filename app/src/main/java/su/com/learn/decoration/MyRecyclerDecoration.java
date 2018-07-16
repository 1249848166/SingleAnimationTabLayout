package su.com.learn.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MyRecyclerDecoration extends RecyclerView.ItemDecoration {
    int space;
    public MyRecyclerDecoration(int space)
    {
        this.space=space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0,space,0,space);
    }
}
