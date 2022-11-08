package kitchenpos.support.extension;

import io.restassured.RestAssured;
import kitchenpos.support.cleaner.DataCleaner;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DataCleanerRandomPortExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(final ExtensionContext context) {
        Integer serverPort = SpringExtension.getApplicationContext(context).getEnvironment()
            .getProperty("local.server.port", Integer.class);

        if (serverPort == null) {
            throw new IllegalStateException("localServerPort cannot be null");
        }
        RestAssured.port = serverPort;

        DataCleaner dataCleaner = getDataCleaner(context);
        dataCleaner.clear();
    }

    private DataCleaner getDataCleaner(final ExtensionContext extensionContext) {
        return (DataCleaner) SpringExtension.getApplicationContext(extensionContext)
            .getBean("dataCleaner");
    }
}
