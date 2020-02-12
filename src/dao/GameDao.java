package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.*;
import response.CreateGameResponse;

import java.util.*;

public class GameDao {

    private static final String GameTable = "Game";
    private static final String GameIdAttr = "gameId";
    private static final String HostIdAttr = "hostId";
    private static final String HostNameAttr = "hostName";
    private static final String Role1Attr = "role1";
    private static final String Role2Attr = "role2";
    private static final String Role3Attr = "role3";

    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-1")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(client);
    Table gameTable = dynamoDB.getTable(GameTable);

    public CreateGameResponse createGame(String hostName) {
        CreateGameResponse response;

        String gameId = UUID.randomUUID().toString().substring(0, 6);
        String hostId = UUID.randomUUID().toString().substring(0, 6);

        Item item = new Item()
                .withPrimaryKey(GameIdAttr, gameId)
                .withString(HostIdAttr, hostId)
                .withString(HostNameAttr, hostName)
                .withBoolean("hasStarted", false)
                .withString(Role1Attr, "tbd")
                .withString(Role2Attr, "tbd")
                .withString(Role3Attr, "tbd");

        try {
            gameTable.putItem(item);
            response = new CreateGameResponse(hostName, hostId, gameId);
        }
        catch (Exception ex) {
            response = new CreateGameResponse("Unable to create game!", "500");
        }

        return response;
    }

    public Boolean isGame(String gameId) {

        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#game", GameIdAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":gameId", new AttributeValue().withS(gameId));

        QueryRequest query = new QueryRequest()
                .withTableName(GameTable)
                .withKeyConditionExpression("#game = :gameId")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues);

        QueryResult result = client.query(query);
        List<Map<String, AttributeValue>> items = result.getItems();

        if (items.size() == 0) {
            return false;
        }

        return true;

    }

    public void gameStarted(String gameId) {
        UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                .withTableName(GameTable)
                .addKeyEntry(GameIdAttr, new AttributeValue().withS(gameId))
                .addAttributeUpdatesEntry("hasStarted", new AttributeValueUpdate().withValue(new AttributeValue().withBOOL(true)));

        client.updateItem(updateItemRequest);
    }

    public String initializePile(String[] roles, String gameId) {
        String message = null;

        UpdateItemSpec update1 = new UpdateItemSpec().withPrimaryKey(GameIdAttr, gameId)
                .withUpdateExpression("set role1=:r")
                .withValueMap(new ValueMap()
                        .withString(":r", roles[0]))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        UpdateItemSpec update2 = new UpdateItemSpec().withPrimaryKey(GameIdAttr, gameId)
                .withUpdateExpression("set role2=:r")
                .withValueMap(new ValueMap()
                        .withString(":r", roles[1]))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        UpdateItemSpec update3 = new UpdateItemSpec().withPrimaryKey(GameIdAttr, gameId)
                .withUpdateExpression("set role3=:r")
                .withValueMap(new ValueMap()
                        .withString(":r", roles[2]))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        try {
            gameTable.updateItem(update1);
            gameTable.updateItem(update2);
            gameTable.updateItem(update3);
        }
        catch (Exception ex) {
            message = "Could not initialize pile!";
        }

        return message;
    }

}
