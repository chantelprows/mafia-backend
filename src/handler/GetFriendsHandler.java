package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.PlayerDao;
import request.GetFriendsRequest;
import response.GetFriendsResponse;

public class GetFriendsHandler {

    public GetFriendsResponse getFriends(GetFriendsRequest request, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering hasFriends");
        PlayerDao playerDao = new PlayerDao();
        String[] friends = playerDao.getFriends(request.getGameId(), request.getPlayerId(), request.getRole());
        GetFriendsResponse response;

        if (friends == null) {
            response = new GetFriendsResponse();
        }
        else {
            response = new GetFriendsResponse(friends);
        }

        return  response;
    }
}
