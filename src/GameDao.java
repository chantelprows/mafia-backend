import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import java.util.UUID;

public class GameDao {

    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    public Game startGame(String hostName) {
        Table table = dynamoDB.getTable("game");

        String gameId = UUID.randomUUID().toString().substring(0, 8);
        String hostId = UUID.randomUUID().toString().substring(0, 8);

        Item item = new Item()
                .withPrimaryKey("gameId", gameId)
                .withString("hostId", hostId)
                .withString("hostName", hostName);

        table.putItem(item);

        return new Game(hostName, hostId, gameId);
    }

}
