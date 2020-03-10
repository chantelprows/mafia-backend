package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.PlayerDao;
import exception.PlayerException;
import request.AddPlayerRequest;
import response.AddPlayerResponse;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class AddPlayerHandler {

    private PlayerDao playerDao = new PlayerDao();



    public AddPlayerResponse addPlayer(AddPlayerRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering addPlayer");
        AddPlayerResponse response;

        String decoded = URLDecoder.decode(request.getPlayerName(), StandardCharsets.UTF_8);

        try {
            response = playerDao.addPlayer(decoded, request.getGameId());
        }
        catch (PlayerException ex) {
            throw new Exception("Internal Server Error: " + ex.toString());
        }

        return response;
    }

}
