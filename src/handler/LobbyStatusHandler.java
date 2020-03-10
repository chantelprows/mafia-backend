package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dao.GameDao;
import dao.PlayerDao;
import model.PlayerCombo;
import request.GameRequest;
import response.LobbyStatusResponse;

public class LobbyStatusHandler {
    private GameDao gameDao = new GameDao();
    private PlayerDao playerDao = new PlayerDao();

    public LobbyStatusResponse getLobbyStatus(GameRequest request, Context context) throws Exception {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering getLobbyStatus");

        LobbyStatusResponse response = new LobbyStatusResponse();
        response.setHasStarted(gameDao.hasStarted(request.getGameId()));
        PlayerCombo pc = playerDao.getPlayers(request.getGameId(), true);
        response.setPlayerNames(pc.getPlayerNames());
        response.setPlayerIds(pc.getPlayerIds());

        return response;
    }
}
