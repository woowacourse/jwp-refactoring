package kitchenpos.test;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestExecutionListeners(
        value = DatabaseCleaner.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@SpringBootTest
public class ServiceTest {
}
