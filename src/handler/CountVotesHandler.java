package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.PlayerDao;
import request.GameRequest;
import response.CountVotesResponse;

public class CountVotesHandler {
    private PlayerDao playerDao = new PlayerDao();

    public CountVotesResponse countVotes(GameRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering countVotes");

        logger.log("Leaving countVotes");
        return new CountVotesResponse(playerDao.countVotes(request.getGameId()));
    }
}
