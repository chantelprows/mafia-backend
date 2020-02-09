package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.PlayerDao;
import request.StartGameRequest;
import response.StartGameResponse;

import java.util.*;

public class StartGameHandler {

    public StartGameResponse startGame(StartGameRequest request, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("entering startGame");
        StartGameResponse response;
        PlayerDao playerDao = new PlayerDao();

        List<String> roles = Arrays.asList(request.getRoles());
        ArrayList<String> players = playerDao.getPlayers(request.getGameId());

        Collections.shuffle(roles);

        int i;
        for (i = 0; i < players.size(); i++) {
            logger.log("giving " + players.get(i) + " " + roles.get(i));
            playerDao.giveRole(players.get(i), roles.get(i), "day");
            playerDao.giveRole(players.get(i), roles.get(i), "night");
        }

        String[] centerRoles =  new String[3];   //declaring array

        for (int j = 0; i < 3; i++) {
            centerRoles[j] = roles.get(i);
        }

        response = new StartGameResponse(centerRoles);

        logger.log("leaving startGame");
        return response;
    }

}
