package response;

public class AddPlayerResponse {
    public String playerId;
    public String gameId;
    public String playerName;
    public String message;
    public String status;

    public AddPlayerResponse(String playerId, String gameId, String playerName) {
        this.playerId = playerId;
        this.gameId = gameId;
        this.playerName = playerName;
        this.message = null;
        this.status = "200";
    }

    public AddPlayerResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }
}
