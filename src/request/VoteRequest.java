package request;

public class VoteRequest {
    public String voterId;
    public String gameId;
    public String voteeId;

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getVoteeId() {
        return voteeId;
    }

    public void setVoteeId(String voteeId) {
        this.voteeId = voteeId;
    }
}
