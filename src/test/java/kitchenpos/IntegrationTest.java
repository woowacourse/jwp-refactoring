package kitchenpos;

import java.util.stream.Stream;
import kitchenpos.common.DatabaseInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntegrationTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    private static Stream<Arguments> getParametersForCreate() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 254; i++) {
            sb.append("a");
        }

        return Stream.of(
            Arguments.of("메뉴 그룹"),
            Arguments.of(sb.toString())
        );
    }
}
