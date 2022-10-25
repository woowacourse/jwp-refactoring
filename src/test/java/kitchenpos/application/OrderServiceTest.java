package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.fake.FakeMenuDao;
import kitchenpos.dao.fake.FakeMenuGroupDao;
import kitchenpos.dao.fake.FakeOrderDao;
import kitchenpos.dao.fake.FakeOrderLineItemDao;
import kitchenpos.dao.fake.FakeOrderTableDao;
import kitchenpos.dao.fake.FakeProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.fixture.MenuFixture;
import kitchenpos.domain.fixture.MenuGroupFixture;
import kitchenpos.domain.fixture.MenuProductFixture;
import kitchenpos.domain.fixture.OrderFixture;
import kitchenpos.domain.fixture.OrderLineItemFixture;
import kitchenpos.domain.fixture.OrderTableFixture;
import kitchenpos.domain.fixture.ProductFixture;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("Order 서비스 테스트")
class OrderServiceTest {

    private OrderService orderService;

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;

    private MenuGroup savedMenuGroup;
    private Product savedProduct;
    private Menu savedMenu;
    private OrderTable savedOrderTable;

    @BeforeEach
    void setUp() {
        final MenuGroupDao menuGroupDao = new FakeMenuGroupDao();
        final ProductDao productDao = new FakeProductDao();
        final MenuDao menuDao = new FakeMenuDao();
        orderDao = new FakeOrderDao();
        orderTableDao = new FakeOrderTableDao();

        orderService = new OrderService(menuDao, orderDao, new FakeOrderLineItemDao(), orderTableDao);

        // menu group
        final MenuGroup menuGroup = MenuGroupFixture.치킨_세트().build();
        savedMenuGroup = menuGroupDao.save(menuGroup);

        // product
        final Product product = ProductFixture.후라이드_치킨()
            .가격(new BigDecimal(15_000))
            .build();
        savedProduct = productDao.save(product);

        // menu
        final Menu menu = MenuFixture.후라이드_치킨_세트()
            .가격(new BigDecimal(30_000))
            .build();
        menu.setMenuGroupId(savedMenuGroup.getId());

        // menu product
        final MenuProduct menuProduct = MenuProductFixture.후라이드()
            .상품_아이디(savedProduct.getId())
            .수량(1)
            .build();
        menu.setMenuProducts(List.of(menuProduct));

        savedMenu = menuDao.save(menu);

        // order table
        final OrderTable orderTable = OrderTableFixture.주문_테이블().build();
        savedOrderTable = orderTableDao.save(orderTable);
    }

    @DisplayName("주문을 등록한다")
    @Test
    void create() {
        final OrderLineItem orderLineItem = OrderLineItemFixture.주문_항목()
            .메뉴_아이디(savedMenu.getId())
            .build();

        final Order order = OrderFixture.주문()
            .주문_테이블_아이디(savedOrderTable.getId())
            .주문_항목들(List.of(orderLineItem))
            .build();

        final Order savedOrder = orderService.create(order);

        assertThat(savedOrder.getId()).isNotNull();
    }

    @DisplayName("주문 등록 시 주문 항목이 비어있으면 안된다")
    @Test
    void createOrderLineItemIsEmpty() {
        final Order order = OrderFixture.주문()
            .주문_항목들(Collections.emptyList())
            .build();

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 주문 항목이 메뉴에 존재해야 한다")
    @Test
    void createOrderLineItemIsNotExist() {
        final OrderLineItem notSavedOrderLineItem = OrderLineItemFixture.주문_항목()
            .메뉴_아이디(savedMenu.getId())
            .build();

        final Order order = OrderFixture.주문()
            .주문_항목들(List.of(notSavedOrderLineItem))
            .build();

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 주문 테이블이 존재해야 한다")
    @Test
    void createOrderTableIsNotExist() {
        final OrderLineItem orderLineItem = OrderLineItemFixture.주문_항목()
            .메뉴_아이디(savedMenu.getId())
            .build();

        long notSavedOrderTableId = 0L;
        final Order order = OrderFixture.주문()
            .주문_테이블_아이디(notSavedOrderTableId)
            .주문_항목들(List.of(orderLineItem))
            .build();

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 주문 테이블이 비어있으면 안된다")
    @Test
    void createOrderTableIsEmpty() {
        final OrderLineItem orderLineItem = OrderLineItemFixture.주문_항목()
            .메뉴_아이디(savedMenu.getId())
            .build();

        final OrderTable orderTable = OrderTableFixture.주문_테이블()
            .빈_테이블(true)
            .build();
        final OrderTable savedEmptyOrderTable = orderTableDao.save(orderTable);

        final Order order = OrderFixture.주문()
            .주문_테이블_아이디(savedEmptyOrderTable.getId())
            .주문_항목들(List.of(orderLineItem))
            .build();

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회한다")
    @Test
    void list() {
        final int numberOfOrder = 5;
        for (int i = 0; i < numberOfOrder; i++) {
            orderDao.save(OrderFixture.주문().build());
        }

        final List<Order> orders = orderService.list();

        assertThat(orders).hasSize(numberOfOrder);
    }

    @DisplayName("주문의 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        final Order order = OrderFixture.주문()
            .주문_테이블_아이디(savedOrderTable.getId())
            .주문한_시간(LocalDateTime.now())
            .주문_상태(OrderStatus.COOKING.name())
            .build();
        final Order savedOrder = orderDao.save(order);

        final Order newOrder = OrderFixture.주문()
            .주문_상태(OrderStatus.COMPLETION.name())
            .build();

        final Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), newOrder);

        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("주문의 상태 변경 시 주문이 존재해야 한다")
    @Test
    void changeOrderStatusOrderIsNotExist() {
        final long notSavedOrderId = 0L;

        assertThatThrownBy(() -> orderService.changeOrderStatus(notSavedOrderId, new Order()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태 변경 시 주문이 완료된 상태면 안된다")
    @Test
    void changeOrderStatusOrderIsCompletion() {
        final Order order = OrderFixture.주문()
            .주문_테이블_아이디(savedOrderTable.getId())
            .주문한_시간(LocalDateTime.now())
            .주문_상태(OrderStatus.COMPLETION.name())
            .build();
        final Order savedOrder = orderDao.save(order);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), new Order()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
