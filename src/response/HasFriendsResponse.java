package response;

public class HasFriendsResponse {
    public String[] friends;

    public HasFriendsResponse(String[] friends) {
        this.friends = friends;
    }

    public HasFriendsResponse() {
        this.friends = null;
    }
}
