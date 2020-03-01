package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.PlayerDao;
import request.HasSeenRoleRequest;
import request.PlayerInfoRequest;
import response.HasCompletedActionResponse;
import response.HasSeenRoleResponse;

public class HasCompletedActionHandler {
    private PlayerDao playerDao = new PlayerDao();

    public HasCompletedActionResponse hasCompletedAction(PlayerInfoRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering hasCompletedAction");

        logger.log("Leaving hasCompletedAction");
        return new HasCompletedActionResponse(playerDao.hasCompletedAction(request.getGameId(), request.getPlayerId()));
    }
}
