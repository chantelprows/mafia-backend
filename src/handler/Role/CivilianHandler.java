package handler.Role;

import dao.PlayerDao;
import exception.PlayerException;
import request.Role.CivilianRequest;
import response.Role.CivilianResponse;

import javax.naming.Context;

public class CivilianHandler {

    public CivilianResponse executeCivilian(CivilianRequest request, Context context) throws PlayerException {
        PlayerDao playerDao = new PlayerDao();
        playerDao.completeAction(request.getGameId(), request.getPlayerId());
        return new CivilianResponse("Success");
    }
}
