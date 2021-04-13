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

    //TODO : i think a concurrency issue happens with these codes in Room class
    @SneakyThrows
    public void run() {
        read();
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
}
