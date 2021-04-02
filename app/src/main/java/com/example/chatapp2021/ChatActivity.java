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

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ChatActivity extends AppCompatActivity {

    private Socket mSocket;
    private Gson gson = new Gson();

    private Button btnSend;
    private EditText etText;

    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    private String userID;
    private String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        roomName = intent.getStringExtra("roomName");
        userID = MainActivity.auto.getString("inputId",null);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<ChatItem> list = new ArrayList<>();

        adapter = new ChatAdapter(list);
        recyclerView.setAdapter(adapter);

        etText = findViewById(R.id.content_edit);

        try {
            mSocket = IO.socket("http://132.226.20.103:9876");
            Log.d("SOCKET", "Connection success : " + mSocket.id());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

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
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
            else if (!userID.equals(data.getFrom())) {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.LEFT_MESSAGE));
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    private void sendMessage() {
        String content = etText.getText().toString();
        if(!content.equals("")) {
            mSocket.emit("newMessage", gson.toJson(new MessageData("MESSAGE",
                    userID,
                    roomName,
                    content,
                    System.currentTimeMillis()
            )));
            adapter.addItem(new ChatItem(userID, content, toDate(System.currentTimeMillis()), ChatType.RIGHT_MESSAGE));
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            etText.setText("");
        }
    }

    private String toDate(long currentMiliis) {
        return new SimpleDateFormat("hh:mm a").format(new Date(currentMiliis));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("left", gson.toJson(new RoomData(userID, roomName)));
        mSocket.disconnect();
    }
}