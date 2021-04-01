package com.example.chatapp2021;

public class RoomData {

    private String userID;
    private String roomName;

    public RoomData(String userID, String roomName) {
        this.userID = userID;
        this.roomName = roomName;
    }

    public String getUserId() {
        return userID;
    }

    public void setUserId(String userId) {
        this.userID = userID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
