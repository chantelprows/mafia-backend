package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
import dao.PlayerDao;
import request.GameRequest;
import request.PlayerInfoRequest;
import response.IsHostResponse;
import response.IsReadyToVoteResponse;

public class IsReadyToVoteHandler {
    private GameDao gameDao = new GameDao();

    public IsReadyToVoteResponse isReadyToVote(GameRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering isReadyToVote");

        boolean readyToVote = gameDao.isReadyToVote(request.getGameId());

        logger.log("Leaving isReadyToVote");
        return new IsReadyToVoteResponse(readyToVote);
    }
}
