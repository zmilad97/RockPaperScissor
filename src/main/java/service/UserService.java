package service;

import controller.Protocols;
import lombok.SneakyThrows;
import model.Player;
import model.Room;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserService extends Thread {

    private Player player;
    private DataOutputStream dos;
    private BufferedReader br;

    public UserService(Player player) {
        this.player = player;
        dos = player.getDos();
        br = player.getBr();
    }

    @SneakyThrows
    public void run() {
        while (player.getSocket().isConnected()) {
            Protocols.parseCommand(br.readLine());
        }
//        try {
//            dos.writeChars("\nEnter Your Name : ");
//            player.setName(br.readLine());
//            String answer = "";
//            dos.writeChars("\nEnter The Number : \n1 - Create a model.Room\n2 - Join a model.Room\n3 - Quit\n");
//            answer = br.readLine();
//            if ((answer).equals("1"))
//                createRoom();
//            else if ((answer).equals("2"))
//                joinRoom();
//            else {
//                System.out.println("\nmodel.Player (" + player.getId() + ") has left !");
//                player.getSocket().close();
//
//            }
//
//        } catch (IOException e) {
//            e.getLocalizedMessage();
//        }
    }

    private void createRoom() throws IOException {
        int str;
        boolean isPublic = true;
        int players = 2;
        String name;
        try {
            dos.writeChars("\nPlease Enter The model.Room Name : ");
            name = br.readLine();
            dos.writeChars("\n1 - public \nOR\n2 - private\n");
            if (br.readLine().equals("2"))
                isPublic = false;
            /**
             * for now it must be a even number
             *  TODO: Make it to use odd numbers too
             */
            dos.writeChars("how many players do you want in room ? at least 2...\n");
            str = Integer.parseInt(br.readLine());
            if (str > 2)
                players = str;
            Room room = new Room(isPublic, players, name);
            GameService.openRooms.put(room.getId(), room);
            room.addPlayer(player);
            room.setAdmin(player);
            GameService.roomPlayerMap.put(room, player);
            dos.writeChars("\nthe room id is : " + room.getId() + "\nshare it with your friends... \n");
        } catch (NumberFormatException e) {
            dos.writeChars("\nyour input is invalid please enter a valid number");
            createRoom();
        }
    }

    private void joinRoom() throws IOException {
        String roomId = "";

        dos.writeChars("\n1 - Show Available rooms\n2 - Enter the room id\n");
        if (br.readLine().equals("1"))
            roomList();
        else {
            dos.writeChars("\n Enter model.Room Id : ");
            roomId = br.readLine();
            GameService.findRoom(roomId).addPlayer(player);
            GameService.roomPlayerMap.put(GameService.findRoom(roomId), player);
        }
    }

    private void roomList() throws IOException {
        List<Room> roomList = new ArrayList<>();
        GameService.openRooms.forEach((k, v) -> {
            if (v.isPublic())
                roomList.add(v);
        });
        roomList.forEach(r -> {
            try {
                dos.writeChars("\n" + roomList.indexOf(r) + " - " + r.getName() + " Made By : " + r.getAdmin() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        dos.writeChars("\nEnter the index of the room you want to join : ");
        Room room = roomList.get(Integer.parseInt(br.readLine()));
        room.addPlayer(player);
        GameService.roomPlayerMap.put(room, player);
    }


}
