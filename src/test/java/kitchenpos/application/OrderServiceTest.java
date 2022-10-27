package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.generateMenu;
import static kitchenpos.fixture.MenuGroupFixture.generateMenuGroup;
import static kitchenpos.fixture.MenuProductFixture.generateMemberProduct;
import static kitchenpos.fixture.OrderFixture.generateOrder;
import static kitchenpos.fixture.OrderLineItemFixture.generateOrderLineItem;
import static kitchenpos.fixture.OrderTableFixture.generateOrderTable;
import static kitchenpos.fixture.ProductFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.FakeMenuDao;
import kitchenpos.dao.FakeMenuGroupDao;
import kitchenpos.dao.FakeMenuProductDao;
import kitchenpos.dao.FakeOrderDao;
import kitchenpos.dao.FakeOrderLineItemDao;
import kitchenpos.dao.FakeOrderTableDao;
import kitchenpos.dao.FakeProductDao;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {

    private OrderService orderService;

    private MenuDao menuDao;
    private OrderDao orderDao;
    private OrderLineItemDao orderLineItemDao;
    private OrderTableDao orderTableDao;
    private MenuGroupDao menuGroupDao;
    private MenuProductDao menuProductDao;
    private ProductDao productDao;

    private Menu 뿌링클_음료두개_세트;

    @BeforeEach
    void beforeEach() {
        this.menuDao = new FakeMenuDao();
        this.orderDao = new FakeOrderDao();
        this.orderLineItemDao = new FakeOrderLineItemDao();
        this.orderTableDao = new FakeOrderTableDao();
        this.menuGroupDao = new FakeMenuGroupDao();
        this.menuProductDao = new FakeMenuProductDao();
        this.productDao = new FakeProductDao();
        this.orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
        Product 사이다 = generateProduct("사이다", 1000);
        Product 뿌링클 = generateProduct("뿌링클", 19000);
        Product 사이다_1L = productDao.save(사이다);
        Product 뿌링클_2L = productDao.save(뿌링클);

        MenuGroup 세트메뉴 = generateMenuGroup("세트메뉴");
        MenuGroup 세트메뉴_1L = menuGroupDao.save(세트메뉴);

        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct 사이다_두개 = menuProductDao.save(generateMemberProduct(사이다_1L, 2));
        MenuProduct 뿌링클_한개 = menuProductDao.save(generateMemberProduct(뿌링클_2L, 1));
        menuProducts.add(사이다_두개);
        menuProducts.add(뿌링클_한개);

        뿌링클_음료두개_세트 = menuDao.save(generateMenu("뿌링클 음료두개 세트", BigDecimal.valueOf(21000), 세트메뉴_1L, menuProducts));
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        // given
        OrderTable emptyOrderTable = generateOrderTable(null, 0, false);
        OrderTable saveOrderTable = orderTableDao.save(emptyOrderTable);

        OrderLineItem orderLineItem = generateOrderLineItem(뿌링클_음료두개_세트.getId(), 1L, null);

        ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        Order order = generateOrder(LocalDateTime.now(),
                saveOrderTable.getId(),
                OrderStatus.COOKING.name(),
                orderLineItems);

        // when
        Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    @DisplayName("주문 생성 시 주문 메뉴 목록이 비어있다면 예외를 반환한다.")
    void create_WhenEmptyOrderLineItems() {
        // given
        OrderTable emptyOrderTable = generateOrderTable(null, 0, false);
        OrderTable saveOrderTable = orderTableDao.save(emptyOrderTable);

        OrderLineItem orderLineItem = generateOrderLineItem(뿌링클_음료두개_세트.getId(), 1L, null);

        Order order = new Order();
        order.setOrderTableId(saveOrderTable.getId());

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 생성 시 메뉴당 하나의 주문 항목이 아니라면 예외를 반환한다.")
    void create_WhenDifferentOrderLineItemsSize() {
        // given
        OrderTable emptyOrderTable = generateOrderTable(null, 0, false);
        OrderTable saveOrderTable = orderTableDao.save(emptyOrderTable);

        OrderLineItem orderLineItem1 = generateOrderLineItem(뿌링클_음료두개_세트.getId(), 1L, null);
        OrderLineItem orderLineItem2 = generateOrderLineItem(뿌링클_음료두개_세트.getId(), 1L, null);

        Order order = new Order();
        order.setOrderTableId(saveOrderTable.getId());
        ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem1);
        orderLineItems.add(orderLineItem2);
        order.setOrderLineItems(orderLineItems);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void list() {
        // given
        OrderTable emptyOrderTable = generateOrderTable(null, 0, false);
        OrderTable saveOrderTable = orderTableDao.save(emptyOrderTable);

        OrderLineItem orderLineItem = generateOrderLineItem(뿌링클_음료두개_세트.getId(), 1L, null);

        Order order = new Order();
        order.setOrderTableId(saveOrderTable.getId());
        ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        order.setOrderLineItems(orderLineItems);

        orderService.create(order);

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        OrderTable emptyOrderTable = generateOrderTable(null, 0, false);
        OrderTable saveOrderTable = orderTableDao.save(emptyOrderTable);

        OrderLineItem orderLineItem = generateOrderLineItem(뿌링클_음료두개_세트.getId(), 1L, null);

        Order order = new Order();
        order.setOrderTableId(saveOrderTable.getId());
        ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        order.setOrderLineItems(orderLineItems);

        Order savedOrder = orderService.create(order);

        // when
        Order changeOrder = orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        // then
        assertThat(changeOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }
}
