import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class TestClass {

    public boolean reached(Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering reached");

        return true;
    }
}
