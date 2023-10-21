package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.추천_메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderLineItemFixture.메뉴을_가진_주문_항목_생성;
import static kitchenpos.fixture.ProductFixture.후추_치킨_10000원;

import java.util.List;
import kitchenpos.DatabaseCleanup;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.OrderLineItemRepository;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.Orders;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.ui.request.OrderCreateRequest;
import kitchenpos.ui.request.OrderLineItemCreateRequest;
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

    protected Orders 주문을_저장하고_반환받는다(OrderTable savedOrderTable) {
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

    protected Orders 주문의_상태를_변환한다(Orders orders, OrderStatus orderStatus) {
        orders.changeOrderStatus(orderStatus);

        return orderRepository.save(orders);
    }

    protected OrderLineItemCreateRequest orderLineItemToCreateRequest(OrderLineItem orderLineItem) {
        return new OrderLineItemCreateRequest(
                orderLineItem.getMenu().getId(),
                orderLineItem.getQuantity()
        );
    }

}
