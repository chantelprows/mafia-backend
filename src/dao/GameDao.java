package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import model.Game;
import response.CreateGameResponse;

import java.util.*;

public class GameDao {

    private static final String GameTable = "Game";

    private static final String GameIdAttr = "gameId";
    private static final String HostIdAttr = "hostId";
    private static final String HostNameAttr = "hostName";

    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-1")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(client);

    public CreateGameResponse createGame(String hostName) {
        Table table = dynamoDB.getTable(GameTable);
        CreateGameResponse response;

        String gameId = UUID.randomUUID().toString().substring(0, 8);
        String hostId = UUID.randomUUID().toString().substring(0, 8);

        Item item = new Item()
                .withPrimaryKey("gameId", gameId)
                .withString("hostId", hostId)
                .withString("hostName", hostName);

        try {
            table.putItem(item);
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
//                .withIndexName(index.getIndexName())
//                .withLimit(3);

        QueryResult result = client.query(query);
        List<Map<String, AttributeValue>> items = result.getItems();

        if (items.size() == 0) {
            return false;
        }

        return true;

    }

}
