package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
import dao.PlayerDao;
import request.GetRoleRequest;
import response.GetRoleResponse;

import java.util.ArrayList;
import java.util.List;

public class GetRoleHandler {
    private PlayerDao playerDao = new PlayerDao();
    private GameDao gameDao = new GameDao();

    public GetRoleResponse getRole(GetRoleRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering getRole");

        GetRoleResponse response = new GetRoleResponse();
        response.setRole(playerDao.getRole(request.getGameId(), request.getPlayerId()));

        List<String> roles = new ArrayList<>();
        List<String> playerRoles = playerDao.getAllRoles(request.getGameId());
        for (int i = 0; i < playerRoles.size(); i++) {
            roles.add(playerRoles.get(i));
        }

        List<String> centerRoles = gameDao.getCenterRoles(request.getGameId());
        for (int i = 0; i < centerRoles.size(); i++) {
            roles.add(centerRoles.get(i));
        }

        response.setAllRoles(roles);
        return response;
    }
}
