package request;

public class HasSeenRoleRequest {
    public String gameId;
    public String playerId;

    public String getGameId(){
        return this.gameId;
    }

    public String getPlayerId(){
        return this.playerId;
    }
}
