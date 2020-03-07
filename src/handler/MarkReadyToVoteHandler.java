package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
import request.GameRequest;
import response.MessageResponse;

public class MarkReadyToVoteHandler {
    private GameDao gameDao = new GameDao();

    public MessageResponse markReadyToVote(GameRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering markReadyToVote");

        gameDao.markReadyToVote(request.getGameId());

        logger.log("Leaving markReadyToVote");
        return new MessageResponse("Success");
    }
}
