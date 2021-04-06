package com.github.zmilad.rps;

import com.github.zmilad97.rps.model.Player;
import com.github.zmilad97.rps.model.PlayerDTO;
import com.github.zmilad97.rps.model.Room;
import com.github.zmilad97.rps.model.RoomDTO;
import com.google.gson.Gson;
import org.junit.Test;

public class GsonTest {

  @Test
  public void testDesrRoom(){
    PlayerDTO admin = new PlayerDTO();
    admin.setId("3");
    RoomDTO roomDTO = new RoomDTO();
    roomDTO.setId("3");
    roomDTO.setName("something");
    roomDTO.setPublic(true);
    roomDTO.setAdmin(admin);
    Gson gson = new Gson();
    System.out.println(gson.toJson(roomDTO));
  }
}
