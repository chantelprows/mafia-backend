package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
import dao.PlayerDao;
import model.Game;
import request.CreateGameRequest;
import response.CreateGameResponse;

public class CreateGameHandler {
    private GameDao gameDao = new GameDao();
    private PlayerDao playerDao = new PlayerDao();

    public CreateGameResponse createGame(CreateGameRequest request, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering startGame");

        CreateGameResponse response = gameDao.createGame(request.getHostName());
        playerDao.addHost(response.getHostName(), response.getHostId(), response.getGameId());

        return response;
    }

}
