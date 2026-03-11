package com.example.myapplication.repository;

import com.example.myapplication.models.Room;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class quản lý dữ liệu (Singleton pattern).
 */
public class RoomRepository {
    private static RoomRepository instance;
    private List<Room> rooms;

    private RoomRepository() {
        rooms = new ArrayList<>();
        // Mock data
        rooms.add(new Room("1", "Phòng 101", 1500000, false, "", ""));
        rooms.add(new Room("2", "Phòng 102", 2000000, true, "Nguyễn Văn A", "0987654321"));
    }

    public static synchronized RoomRepository getInstance() {
        if (instance == null) {
            instance = new RoomRepository();
        }
        return instance;
    }

    public List<Room> getAll() {
        return rooms;
    }

    public void add(Room room) {
        rooms.add(room);
    }

    public void update(Room updatedRoom) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getId().equals(updatedRoom.getId())) {
                rooms.set(i, updatedRoom);
                break;
            }
        }
    }
    public Room getById(String id)
    {
        for (Room room : rooms) {
            if (room.getId().equals(id)) {
                return room;
    }}return null;
    }
    public void delete(String id) {
        rooms.removeIf(room -> room.getId().equals(id));
    }
}
