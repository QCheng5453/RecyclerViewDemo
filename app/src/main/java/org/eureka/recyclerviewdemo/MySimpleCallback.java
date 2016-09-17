package org.eureka.recyclerviewdemo;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by QCheng on 2016/9/17.
 */
public class MySimpleCallback extends ItemTouchHelper.SimpleCallback{
    MyRecyclerViewAdapter adapter;

    public MySimpleCallback(int dragDirs, int swipeDirs, MyRecyclerViewAdapter adapter) {
        super(dragDirs, swipeDirs);
        this.adapter = adapter;
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPos = viewHolder.getAdapterPosition();
        int toPos = target.getAdapterPosition();
        adapter.onItemMove(fromPos,toPos);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
