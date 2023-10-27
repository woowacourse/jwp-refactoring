package execute;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

@Transactional
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SpringBootTest(properties = "spring.sql.init.mode=never")
public class ServiceIntegrateTest {
}
