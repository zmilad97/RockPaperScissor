package com.github.zmilad97.rps.controller;

import com.github.zmilad97.rps.service.PlayerService;
import com.github.zmilad97.rps.service.RoomService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class RoomController {

  private final PlayerService playerService;
  private final RoomService roomService;
  private final Gson gson;

  public RoomController(PlayerService playerService, RoomService roomService, Gson gson) {
    this.playerService = playerService;
    this.roomService = roomService;
    this.gson = gson;
  }

  public String handleLogin(Request request, Response response) {
    String name = gson.fromJson(request.body(), HashMap.class).get("name").toString();
    String playerId = playerService.createPlayer(name);
    return "Hello " + name + " your id is : " + playerId + "\n Join a room with room id or\n Select from available rooms ";
  }

  public String handleCreateRoom(Request request, Response response) {
    return "if you want to play too use join link and this id :" + roomService.CreateRoom(gson.fromJson(request.body(), HashMap.class));
  }
}
