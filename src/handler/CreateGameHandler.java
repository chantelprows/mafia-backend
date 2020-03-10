package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
import dao.PlayerDao;
import request.CreateGameRequest;
import response.CreateGameResponse;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class CreateGameHandler {
    private GameDao gameDao = new GameDao();
    private PlayerDao playerDao = new PlayerDao();

    public CreateGameResponse createGame(CreateGameRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering startGame");

        String decoded = URLDecoder.decode(request.getHostName(), StandardCharsets.UTF_8);

        CreateGameResponse response = gameDao.createGame(decoded);
        playerDao.addHost(response.getHostName(), response.getHostId(), response.getGameId());

        return response;
    }

}
