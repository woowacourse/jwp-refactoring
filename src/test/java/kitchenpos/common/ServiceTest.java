package kitchenpos.common;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@TestExecutionListeners(value = {ServiceTestExecutionListener.class,}, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public abstract class ServiceTest {
}
