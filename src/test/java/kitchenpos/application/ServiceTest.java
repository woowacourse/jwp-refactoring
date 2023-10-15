package kitchenpos.application;

import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@Transactional
@SpringBootTest
@DisplayNameGeneration(ReplaceUnderscores.class)
public class ServiceTest {
}
