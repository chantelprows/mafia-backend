package response;

public class StartGameResponse {
    public String[] centerRoles;
    public String message;
    public String status;

    public StartGameResponse(String[] centerRoles) {
        this.centerRoles = centerRoles;
        this.message = null;
        this.status = "200";
    }

    public StartGameResponse(String message, String status) {
        this.message = message;
        this.status = status;
        this.centerRoles = null;
    }
}
