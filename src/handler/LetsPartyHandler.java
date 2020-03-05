package handler;

import com.amazonaws.services.lambda.runtime.Context;
import dao.GameDao;
import dao.PlayerDao;
import request.LetsPartyRequest;
import request.StartGameRequest;
import response.CreateGameResponse;
import response.EmptyResponse;

public class LetsPartyHandler {
    PlayerDao playerDao = new PlayerDao();
    GameDao gameDao = new GameDao();
    public EmptyResponse letsParty(LetsPartyRequest request, Context context) {
        try {
            CreateGameResponse response = gameDao.createGame("rocky");
            playerDao.addHost("rocky", response.getHostId(), response.getGameId());
            playerDao.addPlayer("chantel", response.getGameId());
            playerDao.addPlayer("connor", response.getGameId());

            if (request.isStartGame()) {
               StartGameRequest startGameRequest =  new StartGameRequest();
               startGameRequest.setGameId(response.getGameId());

                String[] roles = {"mafia", "mafia", "civillian", "civillian", "civillian", "civillian"};
               startGameRequest.setRoles(roles);
               gameDao.startGame(startGameRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new EmptyResponse();
    }
}
