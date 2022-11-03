package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
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
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderServiceTest extends IntegrationTest {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 생성할 때")
    @Nested
    class Create extends IntegrationTest {

        @DisplayName("성공")
        @Test
        void success() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(2, false));
            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹1"));
            Product product = productDao.save(new Product("상품", 1000L));
            MenuProduct menuProduct = new MenuProduct(product, 1);
            Menu menu = menuDao.save(new Menu("메뉴1", new Price(1000L), menuGroup,
                    List.of(menuProduct)));
            List<OrderLineItem> orderLineItems = new ArrayList<>();
            orderLineItems.add(new OrderLineItem(menu, 3));

            // when
            Order order = orderService.create(orderTable.getId(), orderLineItems);

            // then
            assertAll(
                    () -> assertThat(orderDao.findById(order.getId())).isPresent(),
                    () -> assertThat(orderTableDao.findById(order.getOrderTable().getId())).isPresent(),
                    () -> assertThat(menuDao.findById(menu.getId())).isPresent()
            );
        }

        @DisplayName("주문에 속하는 메뉴가 없으면 예외를 발생시킨다.")
        @Test
        void notFoundOrderLineItem_exception() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(2, false));

            // then
            assertThatThrownBy(() -> orderService.create(orderTable.getId(), new ArrayList<>()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 없으면 예외를 발생시킨다.")
        @Test
        void notFoundOrderTable_exception() {
            // given
            ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
            orderLineItems.add(orderLineItemDao.save(new OrderLineItem(null, 3)));

            // then
            assertThatThrownBy(() -> orderService.create(0L, orderLineItems))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("모든 주문을 조회한다.")
    @Test
    void list() {
        // given
        OrderTable orderTable = orderTableDao.save(new OrderTable(2, false));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹1"));
        Product product = productDao.save(new Product("상품", 1000L));
        Menu menu = menuDao.save(
                new Menu("메뉴1", new Price(1000L), menuGroup, List.of(new MenuProduct(product, 3))));
        ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(menu, 3));
        orderDao.save(new Order(orderTable, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItems));

        // when
        List<Order> actual = orderService.list();

        // then
        assertThat(actual).hasSize(1);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Nested
    class ChangeOrderStatus extends IntegrationTest {

        private OrderTable orderTable;
        private Menu menu;
        private List<OrderLineItem> orderLineItems;

        @BeforeEach
        void setUp() {
            orderTable = orderTableDao.save(new OrderTable(2, false));
            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹1"));
            Product product = productDao.save(new Product("상품", 1000L));
            MenuProduct menuProduct = new MenuProduct(product, 1);
            menu = menuDao.save(new Menu("메뉴1", new Price(1000L), menuGroup,
                    List.of(menuProduct)));
            orderLineItems = new ArrayList<>();
            orderLineItems.add(new OrderLineItem(menu, 3));
        }

        @DisplayName("성공")
        @Test
        void success() {
            // given
            Order order = orderDao.save(
                    new Order(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems));

            // when
            Order changedOrder = orderService.changeOrderStatus(order.getId(), OrderStatus.MEAL.name());

            // then
            assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }

        @DisplayName("주문을 찾지 못하면 예외를 발생시킨다.")
        @Test
        void notFoundOrder_exception() {
            // when
            assertThatThrownBy(() -> orderService.changeOrderStatus(0L, OrderStatus.MEAL.name()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 상태가 완료이면 예외를 발생시킨다.")
        @Test
        void orderStatusIsCompletion_exception() {
            // given
            orderDao.save(new Order(orderTable, OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems));

            // when
            assertThatThrownBy(() -> orderService.changeOrderStatus(0L, OrderStatus.MEAL.name()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
