package request;

public class StartGameRequest {
    public String gameId;
    public String[] roles;

    public String getGameId() {
        return gameId;
    }

    public String[] getRoles() {
        return roles;
    }
}
