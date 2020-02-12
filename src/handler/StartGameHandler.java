package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
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
        GameDao gameDao = new GameDao();
        int numCenterRoles = 3;
        List<String> roles = Arrays.asList(request.getRoles());
        ArrayList<String> players = playerDao.getPlayers(request.getGameId(), false);
        String message = null;
        int numPlayers = players.size();
        boolean error = false;

        Collections.shuffle(roles);

        int i;
        for (i = 0; i < numPlayers; i++) {
            if (!playerDao.giveRole(players.get(i), roles.get(i), "day", request.getGameId())) {
                error = true;
            }
            if (!playerDao.giveRole(players.get(i), roles.get(i), "night", request.getGameId())) {
                error = true;
            }
        }

        String[] centerRoles =  new String[numCenterRoles];

        for (int j = 0; j < numCenterRoles; j++) {
            centerRoles[j] = roles.get(i);
            i++;
        }

        if (error) {
            response = new StartGameResponse("Something went wrong while dealing roles!", "500");
            return response;
        }
        else {
            message = gameDao.initializePile(centerRoles, request.getGameId());
            response = new StartGameResponse(message);
        }

        gameDao.gameStarted(request.getGameId());

        logger.log("leaving startGame");
        return response;
    }

}
