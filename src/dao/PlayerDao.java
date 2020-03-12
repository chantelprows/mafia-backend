package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.xspec.S;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import exception.PlayerException;
import model.PlayerCombo;
import response.AddPlayerResponse;
import response.CountVotesResponse;

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
    private static final String VotedForAttr = "votedFor";
    private static final String NightRoleAttr = "nightRole";
    private static final String DayRoleAttr = "dayRole";
    private static final String CompletedActionAttr = "completedAction";
    private static final String VotesAgainstAttr = "votesAgainst";
    private static final String HostNameAttr = "hostName";
    private static final String EmptyValue = "n/a";
    private static final String VotesPresentAttr = "votesPresent";
    private static final String PresentKeyWord = "(present)";

    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-1")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(client);
    private static Table playerTable = dynamoDB.getTable(PlayerTable);
    private static Table gameTable = dynamoDB.getTable(GameTable);

    public void addHost(String hostName, String hostId, String gameId) throws PlayerException {

        List<String> votes = new ArrayList<>();
        Item item = new Item()
                .withPrimaryKey(PlayerIdAttr, hostId, GameIdAttr, gameId)
                .withString(PlayerNameAttr, hostName)
                .withString(NightRoleAttr, EmptyValue)
                .withString(DayRoleAttr, EmptyValue)
                .withBoolean(IsHostAttr, true)
                .withBoolean(SeenRoleAttr, false)
                .withBoolean(CompletedActionAttr, false)
                .withList(VotesAgainstAttr, votes)
                .withString(VotedForAttr, EmptyValue);

        try {
            playerTable.putItem(item);
        }
        catch (Exception ex) {
            throw new PlayerException("Unable to add host");
        }
    }

    public AddPlayerResponse addPlayer(String playerName, String gameId) throws Exception {
        //Table table = dynamoDB.getTable(PlayerTable);
        AddPlayerResponse response;
        GameDao gameDao = new GameDao();
        String playerId = UUID.randomUUID().toString().substring(0, 6);

        if (gameDao.isGame(gameId)) {

            if (isNameTaken(playerName, gameId)) {
                throw new PlayerException("This name is already taken. Pick another!");
            }

            List<String> votes = new ArrayList<>();

            Item item = new Item()
                    .withPrimaryKey(PlayerIdAttr, playerId, GameIdAttr, gameId)
                    .withString(PlayerNameAttr, playerName)
                    .withString(NightRoleAttr, EmptyValue)
                    .withString(DayRoleAttr, EmptyValue)
                    .withBoolean(IsHostAttr, false)
                    .withBoolean(SeenRoleAttr, false)
                    .withBoolean(CompletedActionAttr, false)
                    .withList(VotesAgainstAttr, votes)
                    .withString(VotedForAttr, EmptyValue);

            try {
                playerTable.putItem(item);
                response = new AddPlayerResponse(playerId, gameId, playerName);
            }
            catch (Exception ex) {
                throw new PlayerException("Internal Server Error");
            }
        }
        else {
            throw new PlayerException("Game ID not found");
        }

        return response;
    }

    Boolean isNameTaken(String playerName, String gameId) {

        Index index = playerTable.getIndex("gameId-playerName-index");
        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#game", GameIdAttr);
        attrNames.put("#name", PlayerNameAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":gameId", new AttributeValue().withS(gameId));
        attrValues.put(":playerName", new AttributeValue().withS(playerName));

        QueryRequest query = new QueryRequest()
                .withTableName(PlayerTable)
                .withKeyConditionExpression("#game = :gameId and #name = :playerName")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withIndexName(index.getIndexName());

        QueryResult result = client.query(query);
        List<Map<String, AttributeValue>> items = result.getItems();
//        String existingName;
//        ArrayList<String> players = new ArrayList<>();


        if (items != null) {
            for (Map<String, AttributeValue> item: items) {
                return true;
            }
        }
        return false;
    }

    public PlayerCombo getPlayers(String gameId, boolean getNames) {

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
        ArrayList<String> playerNames = new ArrayList<>();
        ArrayList<String> playerIds = new ArrayList<>();


        if (items != null) {
            for (Map<String, AttributeValue> item: items) {
                if (getNames) {
                    playerId = item.get(PlayerNameAttr).getS();
                    playerNames.add(playerId);
                }
                playerId = item.get(PlayerIdAttr).getS();
                playerIds.add(playerId);
            }
        }

        PlayerCombo pc = new PlayerCombo();
        pc.setPlayerIds(playerIds);
        pc.setPlayerNames(playerNames);

        return pc;

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
        ArrayList<String> playerIds = getPlayers(gameId, false).getPlayerIds();
        Boolean result = true;

        for (String playerId: playerIds) {

            if (!leaveGame(playerId, gameId)) {
                result = false;
            }

        }

        return result;

    }

    public String[] getFriends(String gameId, String playerId, String role) {

        Index index = playerTable.getIndex("gameId-nightRole-index");
        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#game", GameIdAttr);
        attrNames.put("#night", NightRoleAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":gameId", new AttributeValue().withS(gameId));
        attrValues.put(":nightRole", new AttributeValue().withS(role));

        QueryRequest query = new QueryRequest()
                .withTableName(PlayerTable)
                .withKeyConditionExpression("#game = :gameId and #night = :nightRole")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withIndexName(index.getIndexName());

        QueryResult result = client.query(query);
        List<Map<String, AttributeValue>> items = result.getItems();
        String playerName;
        ArrayList<String> names = new ArrayList<>();

        if (items.size() > 0) {
            for (Map<String, AttributeValue> item: items) {
                if (!item.get(PlayerIdAttr).getS().equals(playerId)) {
                    playerName = item.get(PlayerNameAttr).getS();
                    names.add(playerName);
                }
            }
        }

        return names.toArray(new String[0]);

    }

    public String getRole(String gameId, String playerId) {
        Table table = dynamoDB.getTable(PlayerTable);
        Item item = table.getItem(PlayerIdAttr, playerId, GameIdAttr, gameId);

        return item.getString(DayRoleAttr);
    }

    public String getName(String gameId, String playerId) {
        Table table = dynamoDB.getTable(PlayerTable);
        Item item = table.getItem(PlayerIdAttr, playerId, GameIdAttr, gameId);

        return item.getString(PlayerNameAttr);
    }

    public String getNightRole(String gameId, String playerId) {
        Table table = dynamoDB.getTable(PlayerTable);
        Item item = table.getItem(PlayerIdAttr, playerId, GameIdAttr, gameId);

        return item.getString(NightRoleAttr);
    }

    public List<Object> getVotesAgainst(String gameId, String playerId) {
        Table table = dynamoDB.getTable(PlayerTable);
        Item item = table.getItem(PlayerIdAttr, playerId, GameIdAttr, gameId);

        return item.getList(VotesAgainstAttr);
    }

    public List<String> getAllRoles(String gameId) {
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
        String role;
        List<String> roles = new ArrayList<>();

        if (items.size() > 0) {
            for (Map<String, AttributeValue> item: items) {
                    role = item.get(DayRoleAttr).getS();
                    roles.add(role);
            }
        }

        return roles;
    }

    public Boolean vote(String voterId, String voteeId, String gameId) throws PlayerException {

        Item voterItem = playerTable.getItem(PlayerIdAttr, voterId, GameIdAttr, gameId);

        UpdateItemSpec update1;
        UpdateItemSpec update2;

        if (voteeId.equals(PresentKeyWord)) {
            update1 = new UpdateItemSpec().withPrimaryKey(PlayerIdAttr, voterId, GameIdAttr, gameId)
                    .withUpdateExpression("set votedFor=:d")
                    .withValueMap(new ValueMap()
                            .withString(":d", PresentKeyWord))
                    .withReturnValues(ReturnValue.UPDATED_NEW);

            Item game = gameTable.getItem(GameIdAttr, gameId);
            List<Object> votesPresent = game.getList(VotesPresentAttr);
            votesPresent.add(voterItem.getString(PlayerNameAttr));

            update2 = new UpdateItemSpec().withPrimaryKey(GameIdAttr, gameId)
                    .withUpdateExpression("set votesPresent=:d")
                    .withValueMap(new ValueMap()
                            .withList(":d", votesPresent))
                    .withReturnValues(ReturnValue.UPDATED_NEW);

            try {
                playerTable.updateItem(update1);
                gameTable.updateItem(update2);
            } catch (Exception ex) {
                throw new PlayerException("Internal Server Error: Unable to vote!");
            }
        }
        else {
            Item voteeItem = playerTable.getItem(PlayerIdAttr, voteeId, GameIdAttr, gameId);
            List<Object> votes = voteeItem.getList(VotesAgainstAttr);

            update1 = new UpdateItemSpec().withPrimaryKey(PlayerIdAttr, voterId, GameIdAttr, gameId)
                    .withUpdateExpression("set votedFor=:d")
                    .withValueMap(new ValueMap()
                            .withString(":d", voteeItem.getString(PlayerNameAttr)))
                    .withReturnValues(ReturnValue.UPDATED_NEW);

            votes.add(voterItem.getString(PlayerNameAttr));
            update2 = new UpdateItemSpec().withPrimaryKey(PlayerIdAttr, voteeId, GameIdAttr, gameId)
                    .withUpdateExpression("set votesAgainst=:d")
                    .withValueMap(new ValueMap()
                            .withList(":d", votes))
                    .withReturnValues(ReturnValue.UPDATED_NEW);

            try {
                playerTable.updateItem(update1);
                playerTable.updateItem(update2);
            } catch (Exception ex) {
                throw new PlayerException("Internal Server Error: Unable to vote!");
            }
        }
        return isLastVote(gameId);
    }


    public boolean hasVoted(String gameId, String voterId) throws Exception {
        Table table = dynamoDB.getTable(PlayerTable);
        Item item = table.getItem(PlayerIdAttr, voterId, GameIdAttr, gameId);

        String votedFor = item.getString(VotedForAttr);

        return !votedFor.equals(EmptyValue);
    }

    public String getVote(String gameId, String voterId) throws Exception {
        Table table = dynamoDB.getTable(PlayerTable);
        Item item = table.getItem(PlayerIdAttr, voterId, GameIdAttr, gameId);

        return item.getString(VotedForAttr);
    }

    public boolean hasCompletedAction(String gameId, String voterId) throws Exception {
        Table table = dynamoDB.getTable(PlayerTable);
        Item item = table.getItem(PlayerIdAttr, voterId, GameIdAttr, gameId);

        return item.getBoolean(CompletedActionAttr);
    }

    public boolean hasSeenRole(String gameId, String voterId) throws Exception {
        Table table = dynamoDB.getTable(PlayerTable);
        Item item = table.getItem(PlayerIdAttr, voterId, GameIdAttr, gameId);

        return item.getBoolean(SeenRoleAttr);
    }

    public boolean isHost(String gameId, String voterId) throws Exception {
        Table table = dynamoDB.getTable(PlayerTable);
        Item item = table.getItem(PlayerIdAttr, voterId, GameIdAttr, gameId);

        return item.getBoolean(IsHostAttr);
    }

    public boolean completeAction(String gameId, String playerId) throws PlayerException {
        UpdateItemSpec update = new UpdateItemSpec().withPrimaryKey(PlayerIdAttr, playerId, GameIdAttr, gameId)
                .withUpdateExpression("set completedAction=:d")
                .withValueMap(new ValueMap()
                        .withBoolean(":d", true))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        try {
            playerTable.updateItem(update);
        }
        catch (Exception ex) {
            throw new PlayerException("Internal Server Error");
        }
        return isLastAction(gameId);
    }

    public boolean isLastAction(String gameId) {

        GameDao gameDao = new GameDao();
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
                if (!item.get(CompletedActionAttr).getBOOL()) {
                    return false;
                }
            }
        }

        gameDao.markActionsCompleted(gameId);
        return true;
    }

    public boolean isLastVote(String gameId) {

        GameDao gameDao = new GameDao();
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

        if (items != null) {
            for (Map<String, AttributeValue> item: items) {
                if (item.get(VotedForAttr).getS().equals(EmptyValue)) {
                    return false;
                }
            }
        }

        gameDao.markAllVoted(gameId);
        return true;
    }

    public CountVotesResponse countVotes(String gameId) throws Exception {

        ArrayList<String> playerIds = getPlayers(gameId, false).getPlayerIds();
        GameDao gameDao = new GameDao();

        TreeMap<Integer, ArrayList<String>> map = new TreeMap<>();

        for (String playerId: playerIds) {

            String playerName = getName(gameId, playerId);
            List<Object> primitive = getVotesAgainst(gameId, playerId);
            ArrayList<String> votesAgainst = new ArrayList<>();

            for (Object player: primitive) {
                votesAgainst.add((String) player);
            }

            Integer numVotes = votesAgainst.size();
            if (map.containsKey(numVotes)) {
                ArrayList<String> names = map.get(numVotes);
                names.add(playerName);
                map.put(numVotes, names);
            }
            else {
                ArrayList<String> names = new ArrayList<>();
                names.add(playerName);
                map.put(numVotes, names);
            }

        }
        //now add votes for present
        ArrayList<String> votesPresent = new ArrayList<>(gameDao.getVotesPresent(gameId));

        Integer numVotes = votesPresent.size();

        if (map.containsKey(numVotes)) {
            ArrayList<String> names = map.get(numVotes);
            names.add(PresentKeyWord);
            map.put(numVotes, names);
        }
        else {
            ArrayList<String> names = new ArrayList<>();
            names.add(PresentKeyWord);
            map.put(numVotes, names);
        }

        CountVotesResponse response = new CountVotesResponse();
        response.setPlayerName(calculateLoser(map.get(map.lastKey()), gameId));
        if (map.get(map.lastKey()).size() > 1) {

            response.setTiedPlayers(map.get(map.lastKey()));
        }
        return response;
    }

    private boolean mapContains(Map<Integer, ArrayList<String>> map, String name) {
        for (Map.Entry<Integer, ArrayList<String>> entry : map.entrySet()) {
            if (entry.getValue().contains(name)) {
                return true;
            }
        }
        return false;
    }

    private String calculateLoser(ArrayList<String> playerNames, String gameId) {

        ArrayList<String> allPlayers = getPlayers(gameId, false).getPlayerIds();
        String deadPlayer = playerNames.get(0);
        if (deadPlayer.equals(PresentKeyWord)) {
            deadPlayer = playerNames.get(1);
        }

        for (String playerId: allPlayers) {
            Table table = dynamoDB.getTable(PlayerTable);
            if (!playerId.equals(PresentKeyWord)) {
                Item item = table.getItem(PlayerIdAttr, playerId, GameIdAttr, gameId);

                if (playerNames.contains(item.getString(PlayerNameAttr))) {

                    if (item.getString(DayRoleAttr).equals("mafia")) {
                        deadPlayer = item.getString(PlayerNameAttr);
                        break;
                    }

                }
            }
        }

        return deadPlayer;
    }

}
