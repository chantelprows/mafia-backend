package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
import request.StartGameRequest;
import response.MessageResponse;

public class StartGameHandler {
    GameDao gameDao = new GameDao();

    public MessageResponse startGame(StartGameRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("entering startGame");
        gameDao.startGame(request);

        logger.log("leaving startGame");
        return new MessageResponse("Success");
    }

}
