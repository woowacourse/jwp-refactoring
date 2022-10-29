package kitchenpos.support.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import kitchenpos.support.execution.AcceptanceTestExecutionListener;
import kitchenpos.support.extension.DataCleanerExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestExecutionListeners;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(DataCleanerExtension.class)
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public @interface AcceptanceTest {
}

