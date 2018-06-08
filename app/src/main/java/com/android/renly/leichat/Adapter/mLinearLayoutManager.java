package com.android.renly.leichat.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class mLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public mLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
