package kitchenpos.common.support;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DataClearExtension implements AfterEachCallback {

    @Override
    public void afterEach(final ExtensionContext extensionContext) throws Exception {
        DataCleaner dataCleaner = SpringExtension.getApplicationContext(extensionContext)
                .getBean(DataCleaner.class);
        dataCleaner.truncate();
    }
}
