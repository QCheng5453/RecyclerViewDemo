package org.eureka.recyclerviewdemo;

/**
 * Created by QCheng on 2016/9/17.
 */
public class AdapterData {
    // 0 for normal, 1 for expanded
    public int flag = 0;

    // if the itemView expanded or collapsed
    public boolean isExpanded = false;

    // content for normal item
    public String content = "default content";

    public AdapterData(String content) {
        this.content = content;
    }

    public AdapterData(int flag) {
        this.flag = flag;
    }
}
