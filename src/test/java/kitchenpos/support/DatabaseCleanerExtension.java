package kitchenpos.support;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DatabaseCleanerExtension implements AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext context) {
        DatabaseCleaner databaseCleaner = SpringExtension.getApplicationContext(context)
                .getBean(DatabaseCleaner.class);
        databaseCleaner.clear();
    }
}
