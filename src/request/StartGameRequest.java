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

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }
}
