package service;

import controller.Protocols;
import lombok.SneakyThrows;
import model.Player;

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

    public void run() {
//        while (player.getSocket().isConnected()) {
            read();
    }

    @SneakyThrows
    public String read() {
        return protocols.parseCommand(br.readLine());
    }



}
