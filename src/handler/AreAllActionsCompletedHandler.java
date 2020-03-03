package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
import request.GameRequest;
import response.AreAllActionsCompleteResponse;

public class AreAllActionsCompletedHandler {
    private GameDao gameDao = new GameDao();

    public AreAllActionsCompleteResponse areAllActionsComplete(GameRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering areAllActionsComplete");

        boolean areAllActionsComplete = gameDao.areActionsCompleted(request.getGameId());

        logger.log("Leaving areAllActionsComplete");
        return new AreAllActionsCompleteResponse(areAllActionsComplete);
    }
}
