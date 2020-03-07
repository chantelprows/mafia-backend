package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
import dao.PlayerDao;
import request.PlayerInfoRequest;
import response.GetAllRolesResponse;

import java.util.ArrayList;
import java.util.List;

public class GetAllRolesHandler {
    private PlayerDao playerDao = new PlayerDao();
    private GameDao gameDao = new GameDao();

    public GetAllRolesResponse getAllRoles(PlayerInfoRequest request, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering getRole");

        GetAllRolesResponse response = new GetAllRolesResponse();
        response.setRole(playerDao.getRole(request.getGameId(), request.getPlayerId()));

        List<String> playerRoles = playerDao.getAllRoles(request.getGameId());

        List<String> centerRoles = gameDao.getCenterRoles(request.getGameId());

        playerRoles.addAll(centerRoles);

        response.setAllRoles(playerRoles);
        return response;
    }
}
