package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import model.Player;
import response.AddPlayerResponse;

import java.util.*;

public class PlayerDao {

    private static final String PlayerTable = "Player";
    private static final String GameTable = "Game";
    private static final String PlayerIdAttr = "playerId";
    private static final String PlayerNameAttr = "playerName";
    private static final String HostIdAttr = "hostId";
    private static final String GameIdAttr = "gameId";
    private static final String IsHostAttr = "isHost";
    private static final String HostNameAttr = "hostName";

    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-1")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    public void addHost(String hostName, String hostId, String gameId) {
        Table table = dynamoDB.getTable(PlayerTable);


        Item item = new Item()
                .withPrimaryKey(PlayerIdAttr, hostId, GameIdAttr, gameId)
                .withString(PlayerNameAttr, hostName)
                .withBoolean(IsHostAttr, true);

        table.putItem(item);
    }

    public AddPlayerResponse addPlayer(String playerName, String gameId) {
        Table table = dynamoDB.getTable(PlayerTable);
        AddPlayerResponse response;
        GameDao gameDao = new GameDao();
        String playerId = UUID.randomUUID().toString().substring(0, 8);

        if (gameDao.isGame(gameId)) {

            Item item = new Item()
                    .withPrimaryKey(PlayerIdAttr, playerId, GameIdAttr, gameId)
                    .withString(PlayerNameAttr, playerName)
                    .withBoolean(IsHostAttr, false);

            try {
                table.putItem(item);
                response = new AddPlayerResponse(playerId, gameId, playerName);
            }
            catch (Exception ex) {
                response = new AddPlayerResponse("Unable to join game!", "500");
            }
        }
        else {
            response = new AddPlayerResponse("Invalid game ID!", "404");
        }

        return response;
    }
}
