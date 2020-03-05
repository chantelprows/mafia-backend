package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.PlayerDao;
import request.PlayerInfoRequest;
import response.HasSeenRoleResponse;

public class HasSeenRoleHandler {
    private PlayerDao playerDao = new PlayerDao();

    public HasSeenRoleResponse hasSeenRole(PlayerInfoRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering hasSeenRole");

        logger.log("Leaving hasSeenRole");
        return new HasSeenRoleResponse(playerDao.hasSeenRole(request.getGameId(), request.getPlayerId()));
    }
}
