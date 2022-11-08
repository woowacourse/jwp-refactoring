package kitchenpos.support.extension;

import kitchenpos.support.cleaner.DataCleaner;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DataCleanerDefaultExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(final ExtensionContext context) {
        DataCleaner dataCleaner = getDataCleaner(context);
        dataCleaner.clear();
    }

    private DataCleaner getDataCleaner(final ExtensionContext extensionContext) {
        return (DataCleaner) SpringExtension.getApplicationContext(extensionContext)
            .getBean("dataCleaner");
    }
}
