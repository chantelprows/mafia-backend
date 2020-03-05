package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.PlayerDao;
import request.PlayerInfoRequest;
import response.LeaveGameResponse;

public class LeaveGameHandler {

    public LeaveGameResponse leaveGame(PlayerInfoRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering leaveGame");
        PlayerDao playerDao = new PlayerDao();
        LeaveGameResponse response;

        if (playerDao.leaveGame(request.getPlayerId(), request.getGameId())) {
            response = new LeaveGameResponse("Success");
        }
        else {
            throw new Exception("Internal Server Error");
        }

        return response;

    }
}
