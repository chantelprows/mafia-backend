package response;

import java.util.ArrayList;
import java.util.Arrays;

public class CountVotesResponse {
    public String playerName;
    public ArrayList<String> tiedPlayers;

    public CountVotesResponse(String playerName) {
        this.playerName = playerName;
    }

    public CountVotesResponse() {
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public ArrayList<String> getTiedPlayers() {
        return tiedPlayers;
    }

    public void setTiedPlayers(ArrayList<String> tiedPlayers) {

//        ArrayList<String> players = new ArrayList<>();
//
//        players.addAll(Arrays.asList(tiedPlayers));

        this.tiedPlayers = tiedPlayers;
    }
}
