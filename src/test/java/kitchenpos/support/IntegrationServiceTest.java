package kitchenpos.support;

import static java.time.LocalDateTime.now;
import static java.util.Collections.singletonList;
import static kitchenpos.support.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.support.fixture.ProductFixture.product;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.menu.application.ProductService;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.order.presentation.dto.request.OrderLineItemRequest;
import kitchenpos.order.presentation.dto.request.OrderRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.TableRepository;
import kitchenpos.order.repository.TableGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.ProductRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.presentation.dto.request.OrderTableRequest;
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


    // Repository's
    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuProductRepository menuProductRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected OrderLineItemRepository orderLineItemRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected TableRepository tableRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;


    // Support's
    @Autowired
    protected DbTableCleaner dbTableCleaner;

    @PersistenceContext
    protected EntityManager entityManager;

    @BeforeEach
    void integrationSetUp() {
        dbTableCleaner.clearAll();
    }

    protected OrderTableRequest convertTableRequestFrom(OrderTable table) {

        Long tableGroupId = extractTableGroupId(table);

        return new OrderTableRequest(
                table.getId(),
                tableGroupId,
                table.getNumberOfGuests(),
                table.isEmpty()
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

    protected OrderTable 주문테이블_저장후_반환(boolean empty) {
        return tableRepository.save(new OrderTable(4, empty, null));
    }

    protected Menu 메뉴_저장후_반환() {

        MenuGroup 메뉴그룹 = menuGroupRepository.save(메뉴_그룹);
        Product 후라이드_치킨 = productRepository.save(product("후라이드 치킨", 18_000));
        MenuProduct 메뉴상품 = new MenuProduct(후라이드_치킨, 1L);
        Menu 메뉴 = new Menu("[메뉴] 후라이드 치킨 한 마리", BigDecimal.valueOf(18_000), 메뉴그룹, List.of(메뉴상품));
        메뉴.mapMenuProducts(List.of(메뉴상품));

        return menuRepository.save(메뉴);
    }

    protected void 주문_저장() {
        Menu 메뉴 = 메뉴_저장후_반환();
        List<OrderLineItemRequest> 주문항목_요청들 = singletonList(new OrderLineItemRequest(메뉴.getId(), 1L));
        OrderTable 주문테이블 = tableRepository.save(new OrderTable(1L, 1, false, null));
        OrderRequest orderRequest = new OrderRequest(주문테이블.getId(), null, now(), 주문항목_요청들);
        orderService.create(orderRequest);
    }
}
