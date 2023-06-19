package com.lickling.mymusic.ui.load;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lickling.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class LoadActivity extends AppCompatActivity {

    private Toolbar page_head;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<ListItem> listItems;

    private Button loaded_btn;
    private Button loading_btn;
    private View select_tag1;
    private View select_tag2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_loaded);

        page_head = findViewById(R.id.load_navigation);
        page_head.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItems.add(new ListItem("歌曲名", "歌手"));
                recyclerView.setAdapter(listAdapter);
            }
        });

        loaded_btn =findViewById(R.id.loaded_btn);
        select_tag1=findViewById(R.id.select_tag1);
        loaded_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                loaded_btn.setTypeface(null, Typeface.BOLD);
                select_tag1.setVisibility(View.VISIBLE);

                loading_btn.setTypeface(null, Typeface.NORMAL);
                select_tag2.setVisibility(View.INVISIBLE);
            }
        });

        loading_btn =findViewById(R.id.loading_btn);
        select_tag2=findViewById(R.id.select_tag2);
        loading_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading_btn.setTypeface(null, Typeface.BOLD);
                select_tag2.setVisibility(View.VISIBLE);

                loaded_btn.setTypeface(null, Typeface.NORMAL);
                select_tag1.setVisibility(View.INVISIBLE);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        // 在此处添加数据到listItems
        listItems.add(new ListItem("歌曲名", "歌手"));
        listItems.add(new ListItem("歌曲名", "歌手"));
        listItems.add(new ListItem("歌曲名", "歌手"));

        listAdapter = new ListAdapter(listItems, this);
        recyclerView.setAdapter(listAdapter);
    }
}
