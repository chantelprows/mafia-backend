package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
import dao.PlayerDao;
import request.StartGameRequest;
import response.StartGameResponse;

import java.util.*;

public class StartGameHandler {
    GameDao gameDao = new GameDao();

    public StartGameResponse startGame(StartGameRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("entering startGame");
        StartGameResponse response = new StartGameResponse();
        gameDao.startGame(request);

        logger.log("leaving startGame");
        return response;
    }

}
