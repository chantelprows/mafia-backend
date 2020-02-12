package response;

public class EndGameResponse {
    public String message;
    public String status;

    public EndGameResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public EndGameResponse() {
        this.status = "200";
    }
}
