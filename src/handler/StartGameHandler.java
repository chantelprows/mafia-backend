package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
import request.StartGameRequest;
import response.EmptyResponse;

public class StartGameHandler {
    GameDao gameDao = new GameDao();

    public EmptyResponse startGame(StartGameRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("entering startGame");
        EmptyResponse response = new EmptyResponse();
        gameDao.startGame(request);

        logger.log("leaving startGame");
        return response;
    }

}
