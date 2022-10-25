package kitchenpos.common;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DataClearExtension implements AfterEachCallback {

    @Override
    public void afterEach(final ExtensionContext context) {
        DataCleaner dataCleaner = getDataCleanerBean(context);
        dataCleaner.clear();
    }

    private DataCleaner getDataCleanerBean(final ExtensionContext extensionContext) {
        return (DataCleaner) SpringExtension.getApplicationContext(extensionContext)
                .getBean("dataCleaner");
    }
}
