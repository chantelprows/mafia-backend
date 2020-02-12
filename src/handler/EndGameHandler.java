package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
import dao.PlayerDao;
import model.Game;
import request.EndGameRequest;
import response.EndGameResponse;

public class EndGameHandler {

    public EndGameResponse endGame(EndGameRequest request, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering endGame");
        EndGameResponse response = new EndGameResponse();
        GameDao gameDao = new GameDao();
        PlayerDao playerDao = new PlayerDao();

        if (!playerDao.clearLobby(request.getGameId())) {
            response = new EndGameResponse("Unable to clear lobby!", "500");
        }
        if (!gameDao.endGame(request.getGameId())) {
            response = new EndGameResponse("Unable to end game!", "500");
        }

        return response;
    }
}
