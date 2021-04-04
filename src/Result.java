import java.util.List;
import java.util.Map;

public class Result {

    private List<Game> games;
    /**
     *  1 = player1
     *  2 = player2
     *  3 = draw
     */
    private Map<Game,Integer> resultMap;
    private Room room;
}
