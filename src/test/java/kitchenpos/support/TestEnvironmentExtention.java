package kitchenpos.support;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class TestEnvironmentExtention implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        DatabaseManager databaseManager = getSmodyDatabaseManager(context);
        databaseManager.setUp();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        DatabaseManager databaseManager = getSmodyDatabaseManager(context);
        databaseManager.truncateTables();
    }

    private DatabaseManager getSmodyDatabaseManager(ExtensionContext context) {
        return (DatabaseManager) SpringExtension
                .getApplicationContext(context).getBean("databaseManager");
    }
}
