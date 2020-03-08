package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
import dao.PlayerDao;
import request.GameRequest;
import request.PlayerInfoRequest;
import response.HasVotedResponse;
import response.HaveAllVotedResponse;

public class HaveAllVotedHandler {
    private PlayerDao playerDao = new PlayerDao();
    private GameDao gameDao = new GameDao();

    public HaveAllVotedResponse haveAllVoted(GameRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering haveAllVoted");

        Boolean allVoted = gameDao.haveAllVoted(request.getGameId());

        logger.log("Leaving haveAllVoted");
        return new HaveAllVotedResponse(allVoted);
    }
}
