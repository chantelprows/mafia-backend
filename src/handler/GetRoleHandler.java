package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.PlayerDao;
import request.GetRoleRequest;
import response.GetRoleResponse;

public class GetRoleHandler {
    private PlayerDao playerDao = new PlayerDao();

    public GetRoleResponse getRole(GetRoleRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering getRole");

        GetRoleResponse response = new GetRoleResponse();
        response.setRole(playerDao.getRole(request.getGameId(), request.getPlayerId()));

        return response;
    }
}
