package response;

public class StartGameResponse {
    public String message;
    public String status;

    public StartGameResponse(String message) {
        this.message = message;
        this.status = "200";
    }

    public StartGameResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }
}
