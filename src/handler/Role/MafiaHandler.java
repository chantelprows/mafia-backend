package handler.Role;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
import request.Role.MafiaRequest;
import response.RevealCenterRoleResponse;
import response.Role.MafiaResponse;

public class MafiaHandler {

    public MafiaResponse executeMafia(MafiaRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("entering mafia");
        GameDao gameDao = new GameDao();
        String role;

        try {
            role = gameDao.getCenterRole(request.getRoleNum(), request.getGameId());
        }
        catch (Exception ex) {
            throw new Exception("Internal Server Error: Unable to fetch center role");
        }

        logger.log("leaving mafia");
        return new MafiaResponse(role);
    }
}
