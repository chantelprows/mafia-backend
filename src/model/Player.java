package model;

public class Player {
    private String playerId;
    private String gameId;
    private String playerName;
    private String role;
    private boolean isHost;
    private boolean hasPerformedAction;
    private boolean hasSeenRole;
    private boolean hasVoted;

    public Player(String playerId, String gameId, String playerName) {
        this.playerId = playerId;
        this.gameId = gameId;
        this.playerName = playerName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public boolean isHasPerformedAction() {
        return hasPerformedAction;
    }

    public void setHasPerformedAction(boolean hasPerformedAction) {
        this.hasPerformedAction = hasPerformedAction;
    }

    public boolean isHasSeenRole() {
        return hasSeenRole;
    }

    public void setHasSeenRole(boolean hasSeenRole) {
        this.hasSeenRole = hasSeenRole;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
}
