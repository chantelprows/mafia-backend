package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.PlayerDao;
import request.PlayerInfoRequest;
import response.CompleteActionResponse;
import response.HasCompletedActionResponse;

public class CompleteActionHandler {
    private PlayerDao playerDao = new PlayerDao();

    public CompleteActionResponse completeAction(PlayerInfoRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering completeAction");

        boolean isLast = playerDao.completeAction(request.getGameId(), request.getPlayerId());

        logger.log("Leaving completeAction");
        return new CompleteActionResponse(isLast);
    }
}
