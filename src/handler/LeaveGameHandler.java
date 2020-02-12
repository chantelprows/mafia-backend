package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.PlayerDao;
import request.LeaveGameRequest;
import response.LeaveGameResponse;

public class LeaveGameHandler {

    public LeaveGameResponse leaveGame(LeaveGameRequest request, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering leaveGame");
        PlayerDao playerDao = new PlayerDao();
        LeaveGameResponse response;

        if (playerDao.leaveGame(request.getPlayerId(), request.getGameId())) {
            response = new LeaveGameResponse();
        }
        else {
            response = new LeaveGameResponse("Unable to leave game!!", "500");
        }

        return response;

    }
}
