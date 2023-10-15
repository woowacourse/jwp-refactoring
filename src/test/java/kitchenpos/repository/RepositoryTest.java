package kitchenpos.repository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import kitchenpos.support.DatabaseCleanupExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
@TestExecutionListeners(value = DatabaseCleanupExtension.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public @interface RepositoryTest {

}
