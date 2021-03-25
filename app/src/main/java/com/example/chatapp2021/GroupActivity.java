package com.example.chatapp2021;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    private ListView noticeListView;
    private NoticeListAdapter adapter;
    private List<Notice> noticeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        noticeListView = (ListView) findViewById(R.id.noticeListView);
        noticeList = new ArrayList<Notice>();
        noticeList.add(new Notice("공지사항 입니다.", "재현이재현이재현이", "2021-03-26"));
        noticeList.add(new Notice("공지사항 입니다.", "재현이재현이재현이", "2021-03-26"));
        noticeList.add(new Notice("공지사항 입니다.", "재현이재현이재현이", "2021-03-26"));
        noticeList.add(new Notice("공지사항 입니다.", "재현이재현이재현이", "2021-03-26"));
        noticeList.add(new Notice("공지사항 입니다.", "재현이재현이재현이", "2021-03-26"));
        noticeList.add(new Notice("공지사항 입니다.", "재현이재현이재현이", "2021-03-26"));
        noticeList.add(new Notice("공지사항 입니다.", "재현이재현이재현이", "2021-03-26"));
        noticeList.add(new Notice("공지사항 입니다.", "재현이재현이재현이", "2021-03-26"));
        noticeList.add(new Notice("공지사항 입니다.", "재현이재현이재현이", "2021-03-26"));
        adapter = new NoticeListAdapter(getApplicationContext(), noticeList);
        noticeListView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}