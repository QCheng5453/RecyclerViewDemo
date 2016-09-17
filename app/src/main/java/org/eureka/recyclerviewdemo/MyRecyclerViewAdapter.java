package org.eureka.recyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by QCheng on 2016/9/17.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // context from Activity/Fragment
    private Context context;

    // temp data to show on RecyclerView
    //String[] tempContent;

    ArrayList<AdapterData> tempData = new ArrayList<>(20);

    // custom ViewHolder to hold the whole RecyclerView Item
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item_recyclerview;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item_recyclerview = (TextView) itemView.findViewById(R.id.tv_item_recyclerview);

            tv_item_recyclerview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    if (tempData.get(position).isExpanded == false) {
                        tempData.add(position + 1, new AdapterData(1));
                        notifyItemInserted(position + 1);
                        tempData.get(position).isExpanded = true;
                    } else {
                        tempData.remove(position + 1);
                        notifyItemRemoved(position + 1);
                        tempData.get(position).isExpanded = false;
                    }
                }
            });
        }
    }

    // custom another ViewHolder for expanded item
class ExpandedViewHolder extends RecyclerView.ViewHolder {
    ImageView iv_expansion_item_recyclerview;

    public ExpandedViewHolder(View itemView) {
        super(itemView);
        iv_expansion_item_recyclerview = (ImageView) itemView.findViewById(R.id.iv_item_expansion_recyclerview);
    }
}

    public MyRecyclerViewAdapter(Context context) {
        this.context = context;

        // initialize temp data
        /*tempContent = new String[20];
        for (int i = 0; i < 20; i++) {
            tempContent[i] = "item  " + i;
        }*/

        // initialize tempData
        for (int i = 0; i < 20; i++) {
            tempData.add(i, new AdapterData("item  " + i));
        }
    }

    // extend RecyclerView.Adapter must override onCreateViewHolder, onBindViewHolder and getItemCount method

    // called when RecyclerView needs a new RecyclerView.ViewHolder, return RecyclerView.ViewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get LayoutInflater and inflate itemView for RecyclerView
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        switch (viewType) {
            case 1: {
                View itemView = layoutInflater.inflate(R.layout.item_expansion_for_recyclerview_main, parent, false);
                ExpandedViewHolder viewHolder = new ExpandedViewHolder(itemView);
                return viewHolder;
            }
            default: {
                View itemView = layoutInflater.inflate(R.layout.item_for_recyclerview_main, parent, false);

                // create MyViewHolder object from itemView and return it
                MyViewHolder viewHolder = new MyViewHolder(itemView);
                return viewHolder;
            }
        }

    }

    // called by RecyclerView to display the data at the specified position
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 1: {
                ExpandedViewHolder viewHolder = (ExpandedViewHolder) holder;
                viewHolder.iv_expansion_item_recyclerview.setImageResource(R.drawable.img);
                break;
            }
            default: {
                MyViewHolder viewHolder = (MyViewHolder) holder;
                viewHolder.tv_item_recyclerview.setText(tempData.get(position).content);
            }
        }
//        MyViewHolder viewHolder = (MyViewHolder) holder;
//        viewHolder.tv_item_recyclerview.setText(tempContent[position]);
    }

    // return the total number of items in the data set
    @Override
    public int getItemCount() {
        //return tempContent.length;
        return tempData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return tempData.get(position).flag;
    }

    public void onItemMove(int fromPos, int toPos) {
        AdapterData temp = tempData.remove(fromPos);
        tempData.add(fromPos > toPos ? toPos : toPos - 1, temp);
        notifyItemMoved(fromPos,toPos);
    }

    public void onItemDismiss(int posittion) {
        tempData.remove(posittion);
        notifyItemRemoved(posittion);
    }
}
