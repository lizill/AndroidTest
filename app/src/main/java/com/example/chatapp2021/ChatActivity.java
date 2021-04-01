package com.example.chatapp2021;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ChatActivity extends AppCompatActivity {

    private Socket mSocket;
    private String userID;
    private String roomName;
    private Gson gson = new Gson();
    private EditText etText;
    private Button btnSend;
    private ChatAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        etText = (EditText) findViewById(R.id.content_edit);

//        init();


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> myDataset = new ArrayList<>();
        myDataset.add("test1");
        myDataset.add("test2");
        myDataset.add("test3");
        myDataset.add("test4");
        mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stText = etText.getText().toString();

                Hashtable<String, String> numbers = new Hashtable<>();
                numbers.put("email", userID);
                numbers.put("text", stText);

            }
        });
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
        mSocket.on("update", args -> {
           MessageData data = gson.fromJson(args[0].toString(), MessageData.class);
           addChat(data);
        });

        btnSend = (Button) findViewById(R.id.send_btn);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void addChat(MessageData data) {
        runOnUiThread(() -> {
            if (data.getType().equals("ENTER") || data.getType().equals("LEFT")) {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.CENTER_MESSAGE));
//                binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            } else if (data.getType().equals("IMAGE")) {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.LEFT_IMAGE));
//                binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            } else {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.LEFT_MESSAGE));
//                binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    private void sendMessage() {
        mSocket.emit("newMessage", gson.toJson(new MessageData("MESSAGE",
                userID,
                roomName,
                etText.getText().toString(),
                System.currentTimeMillis()
                )));
        adapter.addItem(new ChatItem(userID, etText.getText().toString(), toDate(System.currentTimeMillis()), ChatType.RIGHT_MESSAGE));
//        etText.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        etText.setText("");
    }

    // System.currentTimeMillis를 몇시:몇분 am/pm 형태의 문자열로 반환
    private String toDate(long currentMiliis) {
        return new SimpleDateFormat("hh:mm a").format(new Date(currentMiliis));
    }

    // 이전 버튼을 누를 시 방을 나가고 소켓 통신 종료
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("left", gson.toJson(new RoomData(userID, roomName)));
        mSocket.disconnect();
    }
}