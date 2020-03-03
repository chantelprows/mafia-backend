package handler.Role;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.PlayerDao;
import exception.PlayerException;
import request.Role.CivilianRequest;
import response.Role.CivilianResponse;



public class CivilianHandler {

    public CivilianResponse executeCivilian(CivilianRequest request, Context context) throws PlayerException {
        LambdaLogger logger = context.getLogger();
        logger.log("entering civilian");

        PlayerDao playerDao = new PlayerDao();
        playerDao.completeAction(request.getGameId(), request.getPlayerId());
        return new CivilianResponse("Success");
    }
}
