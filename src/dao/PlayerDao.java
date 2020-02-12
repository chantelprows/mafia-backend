package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
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

    public ArrayList<String> getPlayers(String gameId, boolean getNames) {

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
                if (getNames) {
                    playerId = item.get(PlayerNameAttr).getS();
                    players.add(playerId);
                }
                else {
                    playerId = item.get(PlayerIdAttr).getS();
                    players.add(playerId);
                }
            }
        }

        return players;

    }

    public Boolean giveRole(String playerId, String role, String type, String gameId) {

        playerTable = dynamoDB.getTable(PlayerTable);
        UpdateItemSpec update;

        if (type.equals("day")) {
            update = new UpdateItemSpec().withPrimaryKey(PlayerIdAttr, playerId, GameIdAttr, gameId)
                    .withUpdateExpression("set dayRole=:d")
                    .withValueMap(new ValueMap()
                            .withString(":d", role))
                    .withReturnValues(ReturnValue.UPDATED_NEW);
        }
        else {
            update = new UpdateItemSpec().withPrimaryKey(PlayerIdAttr, playerId, GameIdAttr, gameId)
                    .withUpdateExpression("set nightRole=:n")
                    .withValueMap(new ValueMap()
                            .withString(":n", role))
                    .withReturnValues(ReturnValue.UPDATED_NEW);
        }
        try {
            playerTable.updateItem(update);
            return true;
        }
        catch (Exception ex) {
            return false;
        }

    }

    public Boolean leaveGame(String playerId, String gameId) {

        DeleteItemSpec deleteItem = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey(PlayerIdAttr, playerId, GameIdAttr, gameId));

        // Conditional delete (we expect this to fail)

        try {
            playerTable.deleteItem(deleteItem);
            return true;
        }
        catch (Exception e) {
            return false;
        }

    }

    public Boolean clearLobby(String gameId) {
        // delete all player table items with associated gameId
        ArrayList<String> playerIds = getPlayers(gameId, false);
        Boolean result = true;

        for (String playerId: playerIds) {

            if (!leaveGame(playerId, gameId)) {
                result = false;
            }

        }

        return result;



//        TableWriteItems writer = new TableWriteItems(PlayerTable);
//
//        PrimaryKey primaryKey = new PrimaryKey(GameIdAttr, gameId);
//
//        writer.ke
//
//        try {
//            BatchWriteItemOutcome result = dynamoDB.batchWriteItem();
//
//            do {
//                Map<String, List<WriteRequest>> unprocessedFollower = result.getUnprocessedItems();
//
//                if (unprocessedFollower.size() > 0) {
//                    result = dynamoDB.batchWriteItemUnprocessed(unprocessedFollower);
//                }
//
//            }
//            while (result.getUnprocessedItems().size() > 0);
//
//            return true;
//        }
//        catch (Exception ex) {
//            return false;
//        }
    }

}
