package service;

import com.google.inject.Provides;
import model.Player;

public class PlayerService {

    @Provides
    public void createPlayer(String name){
        Player player = new Player();
        player.setName(name);
    }
}
