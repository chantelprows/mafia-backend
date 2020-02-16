package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.PlayerDao;
import request.HasFriendsRequest;
import response.HasFriendsResponse;

public class HasFriendsHandler {

    public HasFriendsResponse hasFriends(HasFriendsRequest request, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering hasFriends");
        PlayerDao playerDao = new PlayerDao();
        String[] friends = playerDao.getFriends(request.getGameId(), request.getPlayerId(), request.getRole());
        HasFriendsResponse response;

        if (friends == null) {
            response = new HasFriendsResponse();
        }
        else {
            response = new HasFriendsResponse(friends);
        }

        return  response;
    }
}
