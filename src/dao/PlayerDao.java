package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
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
    private static final String SeenRoleAttr = "seenRole";
    private static final String HasVotedAttr = "hasVoted";
    private static final String NightRoleAttr = "nightRole";
    private static final String DayRoleAttr = "dayRole";
    private static final String CompletedActionAttr = "completedAction";
    private static final String HostNameAttr = "hostName";

    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-1")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(client);
    Table playerTable = dynamoDB.getTable(PlayerTable);

    public void addHost(String hostName, String hostId, String gameId) {


        Item item = new Item()
                .withPrimaryKey(PlayerIdAttr, hostId, GameIdAttr, gameId)
                .withString(PlayerNameAttr, hostName)
                .withString(NightRoleAttr, "tbd")
                .withString(DayRoleAttr, "tbd")
                .withBoolean(IsHostAttr, true)
                .withBoolean(SeenRoleAttr, false)
                .withBoolean(CompletedActionAttr, false)
                .withBoolean(HasVotedAttr, false);

        playerTable.putItem(item);
    }

    public AddPlayerResponse addPlayer(String playerName, String gameId) {
        //Table table = dynamoDB.getTable(PlayerTable);
        AddPlayerResponse response;
        GameDao gameDao = new GameDao();
        String playerId = UUID.randomUUID().toString().substring(0, 8);

        if (gameDao.isGame(gameId)) {

            Item item = new Item()
                    .withPrimaryKey(PlayerIdAttr, playerId, GameIdAttr, gameId)
                    .withString(PlayerNameAttr, playerName)
                    .withString(NightRoleAttr, "tbd")
                    .withString(DayRoleAttr, "tbd")
                    .withBoolean(IsHostAttr, false)
                    .withBoolean(SeenRoleAttr, false)
                    .withBoolean(CompletedActionAttr, false)
                    .withBoolean(HasVotedAttr, false);

            try {
                playerTable.putItem(item);
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

    public ArrayList<String> getPlayers(String gameId) {

        Index index = playerTable.getIndex("gameId-index");
        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#game", GameIdAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":gameId", new AttributeValue().withS(gameId));

        QueryRequest query = new QueryRequest()
                .withTableName(PlayerTable)
                .withKeyConditionExpression("#game = :gameId")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withIndexName(index.getIndexName());

        QueryResult result = client.query(query);
        List<Map<String, AttributeValue>> items = result.getItems();
        String playerId;
        ArrayList<String> players = new ArrayList<>();


        if (items != null) {
            for (Map<String, AttributeValue> item: items) {
                playerId = item.get(PlayerIdAttr).getS();
                players.add(playerId);
            }
        }

        return players;

    }

    public Boolean giveRole(String playerId, String role, String type) {

//        UpdateItemSpec update;
////
////        if (type.equals("day")) {
////            update = new UpdateItemSpec().withPrimaryKey(PlayerIdAttr, playerId)
////                    .withUpdateExpression("set dayRole=:p")
////                    .withValueMap(new ValueMap()
////                            .withString(":p", role))
////                    .withReturnValues(ReturnValue.UPDATED_NEW);
////        }
////        else {
////            update = new UpdateItemSpec().withPrimaryKey(PlayerIdAttr, playerId)
////                    .withUpdateExpression("set nightRole=:c")
////                    .withValueMap(new ValueMap()
////                            .withString(":c", role))
////                    .withReturnValues(ReturnValue.UPDATED_NEW);
////        }
////        try {
////            playerTable.updateItem(update);
////            return true;
////        }
////        catch (Exception ex) {
////            return false;
////        }
        return true;

    }

}
