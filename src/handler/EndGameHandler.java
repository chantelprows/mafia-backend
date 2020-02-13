package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
import dao.PlayerDao;
import model.Game;
import request.EndGameRequest;
import response.EndGameResponse;

public class EndGameHandler {

    public EndGameResponse endGame(EndGameRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering endGame");
        EndGameResponse response = new EndGameResponse("Success");
        GameDao gameDao = new GameDao();
        PlayerDao playerDao = new PlayerDao();

        if (!playerDao.clearLobby(request.getGameId())) {
            throw new Exception("Internal Server Error: Clear Lobby Failed");
        }
        if (!gameDao.endGame(request.getGameId())) {
            throw new Exception("Internal Server Error: End Game Failed");
        }

        return response;
    }
}
