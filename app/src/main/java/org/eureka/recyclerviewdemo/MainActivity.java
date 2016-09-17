package org.eureka.recyclerviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_main);

        // use a LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new MySimpleCallback(
                        ItemTouchHelper.DOWN|ItemTouchHelper.UP,
                        ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT,
                        adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
