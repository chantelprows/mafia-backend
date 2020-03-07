package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.PlayerDao;
import exception.PlayerException;
import request.VoteRequest;
import response.MessageResponse;

public class VoteHandler {

    public MessageResponse vote(VoteRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("entering vote");
        PlayerDao playerDao = new PlayerDao();
        try {
            playerDao.vote(request.getVoterId(), request.getVoteeId(), request.getGameId());
        }
        catch (PlayerException ex) {
            throw new Exception("Internal Server Error: " + ex.toString());
        }

        logger.log("leaving vote");
        return new MessageResponse("Success");
    }
}
