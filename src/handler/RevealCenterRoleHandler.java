package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
import request.RevealCenterRoleRequest;
import response.RevealCenterRoleResponse;

public class RevealCenterRoleHandler {

    public RevealCenterRoleResponse revealCenterRole(RevealCenterRoleRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("entering revealCenterRole");
        GameDao gameDao = new GameDao();
        String role;

        try {
            role = gameDao.getCenterRole(request.getRoleNum(), request.getGameId());
        }
        catch (Exception ex) {
            throw new Exception("Internal Server Error: Unable to fetch center role");
        }

        logger.log("leaving revealCenterRole");
        return new RevealCenterRoleResponse(role);
    }
}
