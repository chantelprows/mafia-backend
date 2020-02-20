package response;

public class GetFriendsResponse {
    public String[] friends;
    public String message;

    public GetFriendsResponse(String[] friends) {
        this.friends = friends;
        this.message = null;
    }

    public GetFriendsResponse() {
        this.friends = null;
        this.message = null;
    }
}
