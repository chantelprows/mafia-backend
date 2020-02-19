package response.Role;

public class MafiaResponse {
    public String role;
    public String status;

    public MafiaResponse(String role) {
        this.role = role;
        this.status = "200";
    }

    public MafiaResponse() {
        this.role = null;
        this.status = "200";
    }
}
