package request;

public class RevealCenterRoleRequest {
    public String gameId;
    public int roleNum;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getRoleNum() {
        return roleNum;
    }

    public void setRoleNum(int roleNum) {
        this.roleNum = roleNum;
    }
}
