package response;

public class LeaveGameResponse {
    public String message;
    public String status;

    public LeaveGameResponse() {
        this.message = null;
        this.status = "200";
    }

    public LeaveGameResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }
}
