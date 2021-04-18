package com.github.zmilad97.rps.service;

import com.github.zmilad97.rps.controller.Protocols;
import lombok.SneakyThrows;
import com.github.zmilad97.rps.model.Player;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class UserService extends Thread {
    private final Protocols protocols;
    private final Player player;
    private final BufferedReader br;

    public UserService(Player player) {
        this.player = player;
        this.protocols = new Protocols(player);
        br = player.getBr();
    }

    @SneakyThrows
    public void run() {
        setSocket();
        read();
        //TODO : i think a concurrency issue happens with these codes in Room class
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
        DataOutputStream dos = player.getDos();
        boolean isAuthenticated = false;
        String id = null;
        while (!isAuthenticated) {
            dos.writeChars("\n\nWelcome , Enter your id to continue : ");
            id = br.readLine();
            if (GameService.playerDTOS.get(id) != null)
                isAuthenticated = true;
        }
        this.player.setPlayer(GameService.playerDTOS.get(id));
        dos.writeChars("\n\nHi " + player.getName() + " if you want to see commands list use HELP command\n");
        GameService.players.put(player.getId(), player);
        GameService.playerUserServiceMap.put(player, this);
    }
}
