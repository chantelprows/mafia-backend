package response;

public class RevealCenterRoleResponse {
    public String role;
    public String status;

    public RevealCenterRoleResponse(String role) {
        this.role = role;
        this.status = "200";
    }

    public RevealCenterRoleResponse() {
        this.role = null;
        this.status = "500";
    }
}
