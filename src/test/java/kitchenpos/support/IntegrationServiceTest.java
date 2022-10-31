package kitchenpos.support;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.repository.menu.MenuGroupRepository;
import kitchenpos.repository.order.OrderRepository;
import kitchenpos.repository.order.OrderLineItemRepository;
import kitchenpos.repository.order.OrderTableRepository;
import kitchenpos.repository.order.TableGroupRepository;
import kitchenpos.repository.menu.MenuProductRepository;
import kitchenpos.repository.menu.ProductRepository;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 각 서비스 테스트의 최상단에 붙입니다.
 *
 * 만일 테스트 구조가 Nested Class를 가지면서 (DCI 등)
 *
 * Dao로 추가한 테스트 케이스를 롤백하고자 할 경우 이 클래스를 상속해야 합니다.
s */
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
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
    protected ProductRepository productRepository;

    @Autowired
    protected OrderLineItemRepository orderLineItemRepository;
    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    @Autowired
    protected MenuProductRepository menuProductRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;


    // Support's
    @Autowired
    protected DbTableCleaner dbTableCleaner;

    @PersistenceContext
    protected EntityManager entityManager;

    @BeforeEach
    void setUp() {
        dbTableCleaner.clearAll();
    }

    protected OrderTableRequest convertTableRequestFrom(OrderTable orderTable) {

        Long tableGroupId = extractTableGroupId(orderTable);

        return new OrderTableRequest(
                orderTable.getId(),
                tableGroupId,
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    private Long extractTableGroupId(OrderTable orderTable) {

        Long tableGroupId = null;
        TableGroup tableGroup = orderTable.getTableGroup();

        if (tableGroup != null) {
            tableGroupId = tableGroup.getId();
        }

        return tableGroupId;
    }

    protected OrderTable 주문테이블_저장() {
        return orderTableRepository.save(new OrderTable(4, true, null));
    }
}
