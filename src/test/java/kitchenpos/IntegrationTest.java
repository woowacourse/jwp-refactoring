package kitchenpos;

import java.util.stream.Stream;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.common.DatabaseInitializer;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntegrationTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected TableGroupService tableGroupService;

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
