package response;

public class CreateGameResponse {
    public String hostName;
    public String hostId;
    public String gameId;
    public String message;
    public String status;

    public CreateGameResponse(String hostName, String hostId, String gameId) {
        this.hostName = hostName;
        this.hostId = hostId;
        this.gameId = gameId;
        this.status = "200";
        this.message = null;
    }

    public CreateGameResponse(String message, String status) {
        this.message = message;
        this.status = status;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
