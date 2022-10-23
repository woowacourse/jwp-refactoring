package kitchenpos.application.support;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;

@SpringBootTest
@Retention(RetentionPolicy.RUNTIME)
@TestExecutionListeners(value = {TestExecutionListener.class}, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public @interface IntegrationTest {
}
