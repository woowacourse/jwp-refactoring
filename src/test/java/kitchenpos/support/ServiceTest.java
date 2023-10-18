package kitchenpos.support;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;

@SpringBootTest
@TestExecutionListeners(value = DatabaseCleaner.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ServiceTest {

}
