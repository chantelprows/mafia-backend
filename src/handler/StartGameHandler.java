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
        int numCenterRoles = 3;
        List<String> roles = Arrays.asList(request.getRoles());
        ArrayList<String> players = playerDao.getPlayers(request.getGameId());
        int numRoles = roles.size();
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

        GameDao gameDao = new GameDao();
        gameDao.gameStarted(request.getGameId());

        if (error) {
            response = new StartGameResponse("Something went wrong while dealing roles!", "500");
        }
        else {
            response = new StartGameResponse(centerRoles);
        }

        logger.log("leaving startGame");
        return response;
    }

}
