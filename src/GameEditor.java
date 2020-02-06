import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class GameEditor {
    private GameDao gameDao = new GameDao();
    private PlayerDao playerDao = new PlayerDao();

    public Game startGame(HostName hostName, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering startGame");

        Game game = gameDao.startGame(hostName.getHostName());
        playerDao.addHost(game.getHostName(), game.getHostId(), game.getGameId());

        return game;
    }
}
