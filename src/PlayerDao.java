import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;

public class PlayerDao {

    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    public void addHost(String hostName, String hostId, String gameId) {
        Table table = dynamoDB.getTable("mafiaPlayers");


        Item item = new Item()
                .withPrimaryKey("playerId", hostId, "gameId", gameId)
                .withString("playerName", hostName)
                .withBoolean("isHost", true);

        table.putItem(item);
    }
}
