package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.추천_메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderLineItemFixture.메뉴을_가진_주문_항목_생성;
import static kitchenpos.fixture.ProductFixture.후추_치킨_10000원;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.DatabaseCleanup;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
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
    protected OrderTableDao orderTableDao;

    @Autowired
    protected TableGroupDao tableGroupDao;

    @Autowired
    protected OrderDao orderDao;

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
        Order order = 주문_생성(savedOrderTable, List.of(orderLineItem));

        return orderService.create(order);
    }

    protected Order 주문의_상태를_변환한다(Order order, OrderStatus orderStatus) {
        order.setOrderStatus(orderStatus.name());

        return orderService.changeOrderStatus(order.getId(), order);
    }

}
