package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.PlayerDao;
import request.HasVotedRequest;
import response.HasVotedResponse;
import response.IsHostResponse;

public class IsHostHandler {
    private PlayerDao playerDao = new PlayerDao();

    public IsHostResponse isHost(HasVotedRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering isHost");

        logger.log("Leaving isHost");
        return new IsHostResponse(playerDao.isHost(request.getGameId(), request.getPlayerId()));
    }
}
