package request;

public class StartGameRequest {
    public String gameId;
    public String hostId;
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

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }
}
