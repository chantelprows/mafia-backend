package response;

public class GetFriendsResponse {
    public String[] friends;

    public GetFriendsResponse(String[] friends) {
        this.friends = friends;
    }

    public GetFriendsResponse() {
        this.friends = null;
    }
}
