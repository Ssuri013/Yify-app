package com.android.shubham.yify.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by shubham on 13-Jun-17.
 */

public class PaddingDecorationView extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    public PaddingDecorationView(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public PaddingDecorationView(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
    }
}
