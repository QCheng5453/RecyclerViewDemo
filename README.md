# RecyclerView

Demo url:
[https://github.com/QCheng5453/RecyclerViewDemo](https://github.com/QCheng5453/RecyclerViewDemo)

## Reference
[RecyclerView google guide](https://www.zybuluo.com/mdeditor#503777)

[RecyclerView vogella tutorials](http://www.vogella.com/tutorials/AndroidRecyclerView/article.html#custom-animations)

[RecyclerView drag and swipe tutorial](https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf#.u4sa3dewn)

## Basic Flow
1. Add gradle dependecy
2. Create XML layout files
3. Extend RecyclerView.ViewHolder
4. Extend RecyclerView.Adapter
5. Set LayoutManager

### Gradle dependency

In app gradle:
```java
dependencies {
    // ...
    compile 'com.android.support:recyclerview-v7:24.2.0'
}
```

### Create XML layout files

- Add RecyclerView tag in Activity/Fragment layout file
```xml
<android.support.v7.widget.RecyclerView
    android:id="@+id/rv_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```
- Create Item layout for RecyclerView
```xml
<?xml version="1.0" encoding="utf-8"?>
<TextView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/tv_item_recyclerview"
    android:layout_width="match_parent"
    android:layout_height="40dp">
</TextView>
```

### Custom RecyclerView.Adapter
- Custom ViewHolder as an inner class of custom RecyclerView.Adapter
```java
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // context from Activity/Fragment
    private Context context;

    // temp data to show on RecyclerView
    String[] tempContent;

    // custom ViewHolder to hold the whole RecyclerView Item
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item_recyclerview;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item_recyclerview = (TextView) itemView.findViewById(R.id.tv_item_recyclerview);
        }
    }

    public MyRecyclerViewAdapter(Context context) {
        this.context = context;

        // initialize temp data
        tempContent = new String[20];
        for (int i = 0; i < 20; i++) {
            tempContent[i] = "item  " + i;
        }
    }

    // extend RecyclerView.Adapter must override onCreateViewHolder, onBindViewHolder and getItemCount method

    // called when RecyclerView needs a new RecyclerView.ViewHolder, return RecyclerView.ViewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get LayoutInflater and inflate itemView for RecyclerView
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.item_for_recyclerview_main, parent, false);

        // create MyViewHolder object from itemView and return it
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    // called by RecyclerView to display the data at the specified position
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.tv_item_recyclerview.setText(tempContent[position]);
    }

    // return the total number of items in the data set
    @Override
    public int getItemCount() {
        return tempContent.length;
    }
}
```

### Set LayoutManager and specify adapter
```java
recyclerView = (RecyclerView) findViewById(R.id.rv_main);

// use a LinearLayoutManager
LinearLayoutManager layoutManager = new LinearLayoutManager(this);
recyclerView.setLayoutManager(layoutManager);

// specify an adapter
MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this);
recyclerView.setAdapter(adapter);
```

### Preview
![normal recyclerview.gif-86.4kB][2]

## Implement Expandable RecyclerView

dynamic add or remove RecyclerView item with onClickListener, then notify adapter to refresh data set.

### Create a xml layout file for expanded item
```xml
<?xml version="1.0" encoding="utf-8"?>
<ImageView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="300dp"
    android:id="@+id/iv_item_expansion_recyclerview">
</ImageView>
```

### Create a class to restore data for Adapter item
```java
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
```

### Changes in RecyclerView.Adapter subclass
- create ExpandedViewHolder as inner class to hold expansion item of RecyclerView
```java
class ExpandedViewHolder extends RecyclerView.ViewHolder {
    ImageView iv_expansion_item_recyclerview;

    public ExpandedViewHolder(View itemView) {
        super(itemView);
        iv_expansion_item_recyclerview = (ImageView) itemView.findViewById(R.id.iv_item_expansion_recyclerview);
    }
}
```

- create an ArrayList to restore data set
```java
ArrayList<AdapterData> tempData = new ArrayList<>(20);
```

- override `getItemViewType(int position)`
```java
@Override
public int getItemViewType(int position) {
    // different type means different layout and ViewHolder
    return tempData.get(position).flag;
}
```

- changes in `onCreateViewHolder(ViewGroup parent, int viewTyp)`
```java
// set different ViewHolder according to different viewType
switch (viewType) {
    case 1: { // type for expansion
        View itemView = layoutInflater.inflate(R.layout.item_expansion_for_recyclerview_main, parent, false);
        ExpandedViewHolder viewHolder = new ExpandedViewHolder(itemView);
        return viewHolder;
    }
    default: {
        View itemView = layoutInflater.inflate(R.layout.item_for_recyclerview_main, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }
}
```

- changes in `onBindViewHolder(RecyclerView.ViewHolder holder, int position)`
```java
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
```

- set onClickListener for normal item
```java
// in MyViewHolder constructor
tv_item_recyclerview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    // add item when normal item is not expanded
                    if (tempData.get(position).isExpanded == false) {
                        tempData.add(position + 1, new AdapterData(1));
                        notifyItemInserted(position + 1);
                        tempData.get(position).isExpanded = true;
                    } else {
                    // remove item when normal item is not expanded
                        tempData.remove(position + 1);
                        notifyItemRemoved(position + 1);
                        tempData.get(position).isExpanded = false;
                    }
                }
            });
```

### Preview
![expanded recyclerview.gif-653.8kB][3]

## Implement drag and swipe item

### extend ItemTouchHelper.SimpleCallback
```java
public class MySimpleCallback extends ItemTouchHelper.SimpleCallback{
    // adapter for callback method
    // good way is to create an interface adapter first, then implement it in recyclerview adapter, here is just for convinence
    MyRecyclerViewAdapter adapter;

    public MySimpleCallback(int dragDirs, int swipeDirs, MyRecyclerViewAdapter adapter) {
        super(dragDirs, swipeDirs);
        this.adapter = adapter;
    }
    
    // called when some gesture event made on recyclerview item
    
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPos = viewHolder.getAdapterPosition();
        int toPos = target.getAdapterPosition();
        adapter.onItemMove(fromPos,toPos);
        // true if the viewHolder has been moved to the adapter position of target
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
```
### add callback functions in recyclerview adapter to operate move and dismiss item
```java
public void onItemMove(int fromPos, int toPos) {
    AdapterData temp = tempData.remove(fromPos);
    // if the item at toPos is under the fromPos, 
    tempData.add(fromPos > toPos ? toPos : toPos - 1, temp);
    // notify the adpater that data set has been changed
    notifyItemMoved(fromPos,toPos);
}

public void onItemDismiss(int posittion) {
    tempData.remove(posittion);
    notifyItemRemoved(posittion);
}
```
### attach ItemTouchHelper to the recyclerview
```java
ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
        new MySimpleCallback(
                // respond both down and up touch event
                ItemTouchHelper.DOWN|ItemTouchHelper.UP,
                
                // respond both swipe left and right
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT,
                adapter));
                
                // attach itemTouchHelper to the recyclerView
itemTouchHelper.attachToRecyclerView(recyclerView);
```

### Preview
![drag and swipe recyclerview.gif-130kB][4]

## TODO
- dismiss item by gourp
- withdraw dismissed item
- infinite scrolling, dynamic loading
- think optimizing issues




  [2]: http://static.zybuluo.com/QCheng5453/sf89sq40l18v8d841q6ruolj/normal%20recyclerview.gif
  [3]: http://static.zybuluo.com/QCheng5453/dh5azlxmtejvpqc6dcr4ig00/expanded%20recyclerview.gif
  [4]: http://static.zybuluo.com/QCheng5453/416n1ze2q08b2q998x15gk2p/drag%20and%20swipe%20recyclerview.gif
