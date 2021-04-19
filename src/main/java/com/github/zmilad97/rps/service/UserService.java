package com.github.zmilad97.rps.service;

import com.github.zmilad97.rps.controller.Protocols;
import lombok.SneakyThrows;
import com.github.zmilad97.rps.model.Player;

import java.io.BufferedReader;
import java.io.DataOutputStream;

public class UserService extends Thread {
    private Protocols protocols;
    private Player player;
    private final BufferedReader br;
    private DataOutputStream dos;


    public UserService(Player player) {
        this.player = player;
        this.protocols = new Protocols(player, this);
        br = player.getBr();
        dos = player.getDos();
    }

    @SneakyThrows
    public void run() {
        setSocket();
        read();
        //TODO : i think a concurrency issue might happen with these codes in Room class
        player.getDos().close();
        player.getBr().close();
        player.getSocket().close();
        GameService.players.remove(player.getId());
        GameService.playerUserServiceMap.remove(player);
        if (GameService.playerRoomMap.get(player) != null) {
            GameService.playerRoomMap.get(player).getPlayers().remove(player);
            if (GameService.playerRoomMap.get(player).getRoundPlayerStatusSize() != 0)
                GameService.playerRoomMap.get(player).setPlayerCount(GameService.playerRoomMap.get(player).getPlayerCount() - 1);
        }
    }

    @SneakyThrows
    public void read() {
        String r;
        while (player.getSocket().isConnected()) {
            r = br.readLine();
            if (r == null)
                break;
            protocols.parseCommand(r);
        }

    }


    @SneakyThrows
    public void setSocket() {
        boolean isAuthenticated = false;
        String id = null;
        while (!isAuthenticated) {
            dos.writeChars("\n\nWelcome , Enter your id to continue \n  or use \"name\" protocol and enter your name (NAME XXXX): ");
            id = br.readLine();
            if (id != null)
                if (id.substring(0, 4).equalsIgnoreCase("NAME")) {
                    protocols.parseCommand(id);
                    isAuthenticated = true;
                } else if (GameService.playerDTOS.get(id) != null) {
                    this.player.setPlayer(GameService.playerDTOS.get(id));
                    isAuthenticated = true;
                }
        }
        dos.writeChars("\n\nHi " + player.getName() + " if you want to see commands list use HELP command\n");
        GameService.players.put(player.getId(), player);
        GameService.playerUserServiceMap.put(player, this);
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.protocols = new Protocols(this.player, this);
    }
}
