package com.example.chatapp2021;

public class RoomData {

    private String userId;
    private String roomName;

    public RoomData(String userId, String roomName) {
        this.userId = userId;
        this.roomName = roomName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
