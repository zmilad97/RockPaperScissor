package com.github.zmilad97.rps.service;

import com.github.zmilad97.rps.controller.Protocols;
import lombok.SneakyThrows;
import com.github.zmilad97.rps.model.Player;

import java.io.BufferedReader;

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
        read();
        player.getDos().close();
        player.getBr().close();
        player.getSocket().close();
        GameService.players.remove(player.getId());
        GameService.playerUserServiceMap.remove(player);
        GameService.playerRoomMap.get(player).getPlayers().remove(player);
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
}
