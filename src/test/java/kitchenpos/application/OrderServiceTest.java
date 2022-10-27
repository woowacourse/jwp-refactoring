package kitchenpos.application;

import static kitchenpos.domain.fixture.MenuFixture.후라이드_치킨_세트의_가격과_메뉴_상품_리스트는;
import static kitchenpos.domain.fixture.MenuGroupFixture.치킨_세트;
import static kitchenpos.domain.fixture.MenuProductFixture.상품_하나;
import static kitchenpos.domain.fixture.OrderFixture.완료된_주문;
import static kitchenpos.domain.fixture.OrderFixture.요리중인_주문;
import static kitchenpos.domain.fixture.OrderFixture.주문_1번;
import static kitchenpos.domain.fixture.OrderFixture.주문_1번의_주문_항목들은;
import static kitchenpos.domain.fixture.OrderLineItemFixture.주문_항목_1번;
import static kitchenpos.domain.fixture.OrderTableFixture.비어있는_테이블;
import static kitchenpos.domain.fixture.OrderTableFixture.새로운_테이블;
import static kitchenpos.domain.fixture.ProductFixture.후라이드_치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
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
import kitchenpos.domain.fixture.OrderTableFixture;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("Order 서비스 테스트")
class OrderServiceTest {

    private OrderService orderService;

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;

    private Menu 저장된_후라이드_치킨_세트_메뉴;
    private OrderTable 저장된_주문_테이블;

    @BeforeEach
    void setUp() {
        final MenuGroupDao menuGroupDao = new FakeMenuGroupDao();
        final ProductDao productDao = new FakeProductDao();
        final MenuDao menuDao = new FakeMenuDao();
        orderDao = new FakeOrderDao();
        orderTableDao = new FakeOrderTableDao();

        orderService = new OrderService(menuDao, orderDao, new FakeOrderLineItemDao(), orderTableDao);

        final Product savedProduct = productDao.save(후라이드_치킨());
        final MenuGroup savedMenuGroup = menuGroupDao.save(치킨_세트());

        final MenuProduct menuProduct = 상품_하나(savedProduct.getId());
        final Menu menu = 후라이드_치킨_세트의_가격과_메뉴_상품_리스트는(
            savedMenuGroup.getId(), new BigDecimal(30_000), List.of(menuProduct)
        );

        저장된_후라이드_치킨_세트_메뉴 = menuDao.save(menu);
        저장된_주문_테이블 = orderTableDao.save(OrderTableFixture.새로운_테이블());
    }

    @DisplayName("주문을 등록한다")
    @Test
    void create() {
        final OrderLineItem orderLineItem = 주문_항목_1번(저장된_후라이드_치킨_세트_메뉴.getId());
        final Order order = 주문_1번의_주문_항목들은(저장된_주문_테이블.getId(), List.of(orderLineItem));

        final Order savedOrder = orderService.create(order);

        assertThat(savedOrder.getId()).isNotNull();
    }

    @DisplayName("주문 등록 시 주문 항목이 비어있으면 안된다")
    @Test
    void createOrderLineItemIsEmpty() {
        final Order order = 주문_1번의_주문_항목들은(저장된_주문_테이블.getId(), Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 주문 항목이 메뉴에 존재해야 한다")
    @Test
    void createOrderLineItemIsNotExist() {
        final long notSavedMenuId = 0L;
        final OrderLineItem notSavedOrderLineItem = 주문_항목_1번(notSavedMenuId);
        final Order order = 주문_1번의_주문_항목들은(저장된_주문_테이블.getId(), List.of(notSavedOrderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 주문 테이블이 존재해야 한다")
    @Test
    void createOrderTableIsNotExist() {
        final OrderLineItem orderLineItem = 주문_항목_1번(저장된_후라이드_치킨_세트_메뉴.getId());

        long notSavedOrderTableId = 0L;
        final Order order = 주문_1번의_주문_항목들은(notSavedOrderTableId, List.of(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 주문 테이블이 비어있으면 안된다")
    @Test
    void createOrderTableIsEmpty() {
        final OrderLineItem orderLineItem = 주문_항목_1번(저장된_후라이드_치킨_세트_메뉴.getId());
        final OrderTable savedEmptyOrderTable = orderTableDao.save(비어있는_테이블());

        final Order order = 주문_1번의_주문_항목들은(savedEmptyOrderTable.getId(), List.of(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회한다")
    @Test
    void list() {
        final int numberOfOrder = 5;
        for (int i = 0; i < numberOfOrder; i++) {
            orderDao.save(주문_1번());
        }

        final List<Order> orders = orderService.list();

        assertThat(orders).hasSize(numberOfOrder);
    }

    @DisplayName("주문의 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        final Order order = 요리중인_주문(저장된_주문_테이블.getId());
        final Order savedOrder = orderDao.save(order);
        final Order newOrder = 완료된_주문(저장된_주문_테이블.getId());

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
        final Order order = 완료된_주문(저장된_주문_테이블.getId());
        final Order savedOrder = orderDao.save(order);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), new Order()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
