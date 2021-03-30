package com.example.chatapp2021;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;

import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Hashtable;

public class ChatActivity extends AppCompatActivity {
//    private RecyclerView recyclerView;
//    MyAdapter mAdapter;
//    EditText etText;
//    Button btnSend;
//    String stEmail;
//    private RecyclerView.LayoutManager layoutManager;

    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        try {
            mSocket = IO.socket("http://192.168.0.16:3000");
            mSocket.connect();
            mSocket.on(Socket)
        } catch(URISyntaxException e) {
            e.printStackTrace();
        }

//        stEmail = getIntent().getStringExtra("email");
//        btnSend = (Button) findViewById(R.id.btnSend);
//
//        etText = (EditText) findViewById(R.id.etText);
//
//        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//
//        recyclerView.setHasFixedSize(true);
//
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        ArrayList<String> myDataset = new ArrayList<>();
//        myDataset.add("test1");
//        myDataset.add("test2");
//        myDataset.add("test3");
//        myDataset.add("test4");
//        mAdapter = new MyAdapter(myDataset);
//        recyclerView.setAdapter(mAdapter);
//
//        btnSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String stText = etText.getText().toString();
//
//                Hashtable<String, String> numbers = new Hashtable<>();
//                numbers.put("email", stEmail);
//                numbers.put("text", stText);
//
//            }
//        });
    }
}