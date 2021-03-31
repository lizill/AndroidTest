package com.example.chatapp2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ChatActivity extends AppCompatActivity {
//    private RecyclerView recyclerView;
//    MyAdapter mAdapter;
//    EditText etText;
//    Button btnSend;
//    String stEmail;
//    private RecyclerView.LayoutManager layoutManager;

    private Socket mSocket;
    private String userID;
    private String roomName;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();

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

    private void init() {
        try {
            mSocket = IO.socket("http://10.0.2.2:80");
            Log.d("SOCKET", "Connection success : " + mSocket.id());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        roomName = intent.getStringExtra("roomName");

        mSocket.connect();

        mSocket.on(Socket.EVENT_CONNECT, args -> {
            mSocket.emit("enter", gson.toJson(new RoomData(userID, roomName)));
        });
//        mSocket.on("update", args -> {
//           MessageData data = gson.fromJson(args[0].toString(), MessageData.class);
//        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}