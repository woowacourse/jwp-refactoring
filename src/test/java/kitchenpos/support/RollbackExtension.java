package kitchenpos.support;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class RollbackExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        DatabaseCleaner databaseCleaner = SpringExtension.getApplicationContext(context).getBean(DatabaseCleaner.class);
        databaseCleaner.clear();
    }
}
