package kitchenpos.support.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import kitchenpos.support.execution.AcceptanceTestServerPortExecution;
import kitchenpos.support.extension.DataCleanerExtension;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestExecutionListeners;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Retention(RetentionPolicy.RUNTIME)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(DataCleanerExtension.class)
@TestExecutionListeners(value = {AcceptanceTestServerPortExecution.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public @interface AcceptanceTest {
}
