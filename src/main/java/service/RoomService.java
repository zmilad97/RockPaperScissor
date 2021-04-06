package service;


import com.google.inject.Provides;
import model.Room;


public class RoomService {

    @Provides
    public void CreateRoom(Room room){
        Room newRoom = room;
    }
}
