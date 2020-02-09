import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class TestEndpoint {

    public boolean test(Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering test");

        return true;
    }
}
