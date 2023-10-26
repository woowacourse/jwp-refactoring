package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.추천_메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.OrderLineItemFixture.메뉴을_가진_주문_항목_생성;
import static kitchenpos.fixture.ProductFixture.후추_치킨_10000원;

import java.util.List;
import kitchenpos.DatabaseCleanup;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
public abstract class ServiceIntegrationTest {

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected MenuProductRepository menuProductRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderService orderService;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void beforeEach() {
        databaseCleanup.execute();
    }

    protected Order 주문을_저장하고_반환받는다(OrderTable savedOrderTable) {
        Product savedProduct = productRepository.save(후추_치킨_10000원());
        MenuGroup savedMenuGroup = menuGroupRepository.save(추천_메뉴_그룹());
        MenuProduct menuProduct = 메뉴_상품(savedProduct, 2);
        Menu savedMenu = menuRepository.save(메뉴_생성(20000L, savedMenuGroup, menuProduct));
        OrderLineItem orderLineItem = 메뉴을_가진_주문_항목_생성(savedMenu, 2);
        OrderCreateRequest request = new OrderCreateRequest(
                savedOrderTable.getId(),
                List.of(orderLineItemToCreateRequest(orderLineItem))
        );

        return orderService.create(request);
    }

    protected Order 주문의_상태를_변환한다(Order order, OrderStatus orderStatus) {
        order.changeOrderStatus(orderStatus);

        return orderRepository.save(order);
    }

    protected OrderLineItemCreateRequest orderLineItemToCreateRequest(OrderLineItem orderLineItem) {
        return new OrderLineItemCreateRequest(
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity()
        );
    }

}
