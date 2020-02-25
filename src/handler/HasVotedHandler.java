package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
import dao.PlayerDao;
import request.HasVotedRequest;
import response.HasVotedResponse;
import response.LobbyStatusResponse;

import java.util.ConcurrentModificationException;

public class HasVotedHandler {
    private GameDao gameDao = new GameDao();
    private PlayerDao playerDao = new PlayerDao();

    public HasVotedResponse hasVoted(HasVotedRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering hasVoted");

        HasVotedResponse response = new HasVotedResponse(playerDao.hasVoted(request.getGameId(), request.getPlayerId()));

        return response;
    }
}
