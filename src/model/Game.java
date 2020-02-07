package model;

public class Game {
    private String hostName;
    private String hostId;
    private String gameId;

    public Game(String hostName, String hostId, String gameId) {
        this.hostName = hostName;
        this.hostId = hostId;
        this.gameId = gameId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
