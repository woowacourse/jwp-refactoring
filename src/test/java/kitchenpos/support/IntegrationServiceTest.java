package kitchenpos.support;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * 각 서비스 테스트의 최상단에 붙입니다.
 *
 * 만일 테스트 구조가 Nested Class를 가지면서 (DCI 등)
 *
 * Dao로 추가한 테스트 케이스를 롤백하고자 할 경우 이 클래스를 상속해야 합니다.
s */
@SpringBootTest
@Transactional // Transaction Rollback in Test
abstract public class IntegrationServiceTest {

    // Service's

    // Product ~ Menu
    @Autowired
    protected ProductService productService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected MenuGroupService menuGroupService;

    // Order
    @Autowired
    protected OrderService orderService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;


    // DAO's
    @Autowired
    protected OrderLineItemDao orderLineItemDao;
    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected TableGroupDao tableGroupDao;


    // Support's
    @Autowired
    protected DbTableCleaner dbTableCleaner;

    @AfterEach
    void tearDown() {
//        dbTableCleaner.clear();
    }
}
