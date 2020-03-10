package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.*;
import request.StartGameRequest;
import response.CreateGameResponse;

import java.util.*;

public class GameDao {

    private static final String GameTable = "Game";
    private static final String GameIdAttr = "gameId";
    private static final String HostIdAttr = "hostId";
    private static final String HostNameAttr = "hostName";
    private static final String HasStartedAttr = "hasStarted";
    private static final String CompletedActionsAttr = "completedActions";
    private static final String Role1Attr = "role1";
    private static final String Role2Attr = "role2";
    private static final String Role3Attr = "role3";
    private static final String KilledAttr = "killed";
    private static final String ReadyToVoteAttr = "readyToVote";
    private static final String PlayerTable = "Player";
    private static final String AllVotedAttr = "allVoted";
    private static final String EmptyValue = "n/a";

    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-1")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(client);
    private Table gameTable = dynamoDB.getTable(GameTable);

    public CreateGameResponse createGame(String hostName) throws Exception {
        CreateGameResponse response;

        String gameId = UUID.randomUUID().toString().substring(0, 4);
        String hostId = UUID.randomUUID().toString().substring(0, 6);

        Item item = new Item()
                .withPrimaryKey(GameIdAttr, gameId)
                .withString(HostIdAttr, hostId)
                .withString(HostNameAttr, hostName)
                .withBoolean(HasStartedAttr, false)
                .withString(Role1Attr, EmptyValue)
                .withString(Role2Attr, EmptyValue)
                .withString(Role3Attr, EmptyValue)
                .withString(KilledAttr, EmptyValue)
                .withBoolean(CompletedActionsAttr, false)
                .withBoolean(ReadyToVoteAttr, false)
                .withBoolean(AllVotedAttr, false);

        try {
            gameTable.putItem(item);
            response = new CreateGameResponse(hostName, hostId, gameId);
        }
        catch (Exception ex) {
            throw new Exception("Internal Server Error");
        }

        return response;
    }

    public void markActionsCompleted(String gameId) {
        UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                .withTableName(GameTable)
                .addKeyEntry(GameIdAttr, new AttributeValue().withS(gameId))
                .addAttributeUpdatesEntry(CompletedActionsAttr, new AttributeValueUpdate().withValue(new AttributeValue().withBOOL(true)));

        client.updateItem(updateItemRequest);
    }

    public void markReadyToVote(String gameId) {
        UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                .withTableName(GameTable)
                .addKeyEntry(GameIdAttr, new AttributeValue().withS(gameId))
                .addAttributeUpdatesEntry(ReadyToVoteAttr, new AttributeValueUpdate().withValue(new AttributeValue().withBOOL(true)));

        client.updateItem(updateItemRequest);
    }

    public void markAllVoted(String gameId) {
        UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                .withTableName(GameTable)
                .addKeyEntry(GameIdAttr, new AttributeValue().withS(gameId))
                .addAttributeUpdatesEntry(AllVotedAttr, new AttributeValueUpdate().withValue(new AttributeValue().withBOOL(true)));

        client.updateItem(updateItemRequest);
    }

    public boolean areActionsCompleted(String gameId) throws Exception {
        Table table = dynamoDB.getTable(GameTable);
        try {
            Item item = table.getItem(GameIdAttr, gameId);
            return item.getBoolean(CompletedActionsAttr);
        }
        catch (Exception ex) {
            throw new Exception("Internal Server Error. Unable to check if all actions are completed.");
        }
    }

    public boolean haveAllVoted(String gameId) throws Exception {
        Table table = dynamoDB.getTable(GameTable);
        try {
            Item item = table.getItem(GameIdAttr, gameId);
            return item.getBoolean(AllVotedAttr);
        }
        catch (Exception ex) {
            throw new Exception("Internal Server Error. Unable to check if all players have voted.");
        }
    }

    public boolean isReadyToVote(String gameId) throws Exception {
        Table table = dynamoDB.getTable(GameTable);
        try {
            Item item = table.getItem(GameIdAttr, gameId);
            return item.getBoolean(ReadyToVoteAttr);
        }
        catch (Exception ex) {
            throw new Exception("Internal Server Error. Unable to check if game is ready to vote.");
        }
    }

    public List<String> getCenterRoles(String gameId) {
        Table table = dynamoDB.getTable(GameTable);
        Item item = table.getItem("gameId", gameId);

        List<String> roles = new ArrayList<>();

        roles.add(item.getString("role1"));
        roles.add(item.getString("role2"));
        roles.add(item.getString("role3"));

        return roles;
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

    public boolean hasStarted(String gameId) throws Exception {
        Table table = dynamoDB.getTable("Game");
        try {
            Item item = table.getItem("gameId", gameId);
            return item.getBoolean("hasStarted");
        }
        catch (Exception ex) {
            throw new Exception("Internal Server Error");
        }
    }

    public String getCenterRole(int roleNum, String gameId) throws Exception {
        Table table = dynamoDB.getTable("Game");
        Item item = table.getItem("gameId", gameId);

        try {
            if (roleNum == 1) {
                return item.getString("role1");
            }
            else if (roleNum == 2) {
                return item.getString("role2");
            }
            else {
                return item.getString("role3");
            }
        }
        catch (Exception ex) {
            throw new Exception("Internal Server Error");
        }
    }

    public void initializePile(String[] roles, String gameId) throws Exception {
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
            throw new Exception("Internal Server Error");
        }
    }
    public void startGame(StartGameRequest request) throws Exception {
        PlayerDao playerDao = new PlayerDao();
        GameDao gameDao = new GameDao();
        int numCenterRoles = 3;
        List<String> primitiveList = Arrays.asList(request.getRoles());
        ArrayList<String> roles = new ArrayList<>(primitiveList);
        ArrayList<String> players = playerDao.getPlayers(request.getGameId(), false);

        String hostName = playerDao.getName(request.getGameId(), request.getHostId());
        int numPlayers = players.size();
        boolean error = false;

        if (hostName.equals("AlCapone")) {
            if (!playerDao.giveRole(request.getHostId(), "mafia", "day", request.getGameId())) {
                error = true;
            }
            if (!playerDao.giveRole(request.getHostId(), "mafia", "night", request.getGameId())) {
                error = true;
            }

            roles.remove("mafia");
            players.remove(request.getHostId());
        }
        //else {

            Collections.shuffle(roles);

            int i;
            for (i = 0; i < players.size(); i++) {
                if (!playerDao.giveRole(players.get(i), roles.get(i), "day", request.getGameId())) {
                    error = true;
                }
                if (!playerDao.giveRole(players.get(i), roles.get(i), "night", request.getGameId())) {
                    error = true;
                }
            }

            String[] centerRoles = new String[numCenterRoles];

            for (int j = 0; j < numCenterRoles; j++) {
                centerRoles[j] = roles.get(i);
                i++;
            }

            if (error) {
                throw new Exception("Internal Server Error");
            } else {
                gameDao.initializePile(centerRoles, request.getGameId());
            }

            gameDao.gameStarted(request.getGameId());
        //}
    }

    public Boolean endGame(String gameId) {

        //first delete game table item
        DeleteItemSpec deleteItem = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey(GameIdAttr, gameId));

        try {
            gameTable.deleteItem(deleteItem);
            return true;
        }
        catch (Exception e) {
            return false;
        }

    }

}
