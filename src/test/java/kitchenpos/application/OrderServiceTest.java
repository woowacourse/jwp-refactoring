package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
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

@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuDao menuDao;

    MenuGroup menuGroup;
    Menu menu;
    Product product1;
    Product product2;
    OrderTable orderTable;

    @BeforeEach
    void setUp() {
        MenuGroup newMenuGroup = new MenuGroup("두마리메뉴");
        menuGroup = menuGroupDao.save(newMenuGroup);

        Product newProduct1 = new Product("후라이드", new BigDecimal(16_000));
        product1 = productDao.save(newProduct1);
        Product newProduct2 = new Product("양념치킨", new BigDecimal(16_000));
        product2 = productDao.save(newProduct2);

        BigDecimal price = new BigDecimal(30_000);
        Menu newMenu = new Menu("후라이드, 양념치킨 2마리 세트", price, menuGroup.getId(),
                List.of(new MenuProduct(1L, product1.getId(), 1), new MenuProduct(1L, product2.getId(), 1)));
        menu = menuDao.save(newMenu);

        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(false);
        orderTable = orderTableDao.save(newOrderTable);
    }

    @DisplayName("주문을 생성한다")
    @Nested
    class CreateTest {

//        @BeforeEach
//        void setUp() {
//            changeTableToNotEmpty(orderTable);
//        }

        @DisplayName("주문을 생성하면 ID가 할당된 Order객체가 반환된다")
        @Test
        void create() {
            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(), 3));
            Order order = new Order(orderTable.getId(), orderLineItems);

            Order actual = orderService.create(order);
            assertThat(actual).isNotNull();
        }

        @DisplayName("orderLineItems이 비어있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfEmptyOrderLineItems() {
            Order order = new Order(orderTable.getId(), List.of());

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("orderLineItems에 존재하지 않는 메뉴가 있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistMenu() {
            Long notExistId = 0L;
            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(notExistId, 3));
            Order order = new Order(orderTable.getId(), orderLineItems);

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 테이블일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistTable() {
            Long notExistId = 0L;
            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(), 3));
            Order order = new Order(notExistId, orderLineItems);

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("비어있는 테이블일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfEmptyTable() {
            OrderTable newEmptyTable = new OrderTable();
            newEmptyTable.setEmpty(true);
            OrderTable emptyTable = orderTableDao.save(newEmptyTable);

            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(), 3));
            Order order = new Order(emptyTable.getId(), orderLineItems);

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 목록을 조회한다")
    @Test
    void list() {
        List<Order> actual = orderService.list();

        assertThat(actual).hasSize(0);
    }

    @DisplayName("주문 상태를 변경한다")
    @Nested
    class ChangeOrderStatusTest {

        @DisplayName("주문 상태를 변경한다")
        @Test
        void changeOrderStatus() {
            Order order = createOrder();
            String expectedOrderStatus = OrderStatus.COMPLETION.name();

            Order orderToChangeCompleteStatus = createOrder();
            orderToChangeCompleteStatus.setOrderStatus(expectedOrderStatus);
            Order changedOrder = orderService.changeOrderStatus(order.getId(), orderToChangeCompleteStatus);

            assertThat(changedOrder.getOrderStatus()).isEqualTo(expectedOrderStatus);
        }

        @DisplayName("존재하지 않는 주문일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistOrder() {
            Long notExistId = 0L;
            Order orderToUpdate = createOrder();

            assertThatThrownBy(() -> orderService.changeOrderStatus(notExistId, orderToUpdate))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Complete상태의 주문은 변경할 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfCompletedOrder() {
            Order order = createOrder();

            Order orderToChangeCompleteStatus = createOrder();
            orderToChangeCompleteStatus.setOrderStatus(OrderStatus.COMPLETION.name());
            orderService.changeOrderStatus(order.getId(), orderToChangeCompleteStatus);

            Order orderToChange = createOrder();
            orderToChange.setOrderStatus(OrderStatus.MEAL.name());

            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderToChange))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private Order createOrder() {
            Long orderTableId = orderTable.getId();
            changeTableToNotEmpty(orderTableId);

            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(), 3));
            Order order = new Order(orderTableId, orderLineItems);

            return orderService.create(order);
        }
    }

    void changeTableToNotEmpty(Long tableId) {
        OrderTable orderTable = orderTableDao.findById(tableId)
                .orElseThrow();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);

        orderTableDao.save(orderTable);
    }
}
