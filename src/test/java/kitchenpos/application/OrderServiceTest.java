package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fakedao.MenuFakeDao;
import kitchenpos.fakedao.MenuGroupFakeDao;
import kitchenpos.fakedao.OrderFakeDao;
import kitchenpos.fakedao.OrderLineItemFakeDao;
import kitchenpos.fakedao.OrderTableFakeDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {

    private MenuDao menuDao = new MenuFakeDao();
    private MenuGroupDao menuGroupDao = new MenuGroupFakeDao();
    private OrderDao orderDao = new OrderFakeDao();
    private OrderLineItemDao orderLineItemDao = new OrderLineItemFakeDao();
    private OrderTableDao orderTableDao = new OrderTableFakeDao();

    private OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

    @DisplayName("주문을 생성할 때")
    @Nested
    class Create {

        @DisplayName("성공")
        @Test
        void success() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(null, 2, false));
            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹1"));
            Menu menu = menuDao.save(new Menu("메뉴1", BigDecimal.valueOf(1000), menuGroup.getId(), new ArrayList<>()));
            ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
            orderLineItems.add(orderLineItemDao.save(new OrderLineItem(menu.getId(), 3)));
            // when
            Order order = orderService.create(new Order(
                    orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems)
            );

            // then
            assertAll(
                    () -> assertThat(orderDao.findById(order.getId())).isPresent(),
                    () -> assertThat(orderTableDao.findById(order.getOrderTableId())).isPresent(),
                    () -> assertThat(menuDao.findById(menu.getId())).isPresent()
            );
        }

        @DisplayName("주문에 속하는 메뉴가 없으면 예외를 발생시킨다.")
        @Test
        void notFoundOrderLineItem_exception() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(null, 2, false));

            // then
            assertThatThrownBy(() -> orderService.create(new Order(
                    orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>()
            ))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴를 찾지 못하면 예외를 발생시킨다.")
        @Test
        void notFoundMenu_exception() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(null, 2, false));
            ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
            orderLineItems.add(orderLineItemDao.save(new OrderLineItem(0L, 3)));

            // then
            assertThatThrownBy(() -> orderService.create(new Order(
                    orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems
            ))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 없으면 예외를 발생시킨다.")
        @Test
        void notFoundOrderTable_exception() {
            // given
            ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
            orderLineItems.add(orderLineItemDao.save(new OrderLineItem(0L, 3)));

            // then
            assertThatThrownBy(() -> orderService.create(new Order(
                    0L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems
            ))).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("모든 주문을 조회한다.")
    @Test
    void list() {
        // given
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 2, false));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹1"));
        Menu menu = menuDao.save(new Menu("메뉴1", BigDecimal.valueOf(1000), menuGroup.getId(), new ArrayList<>()));
        ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItemDao.save(new OrderLineItem(menu.getId(), 3)));
        orderDao.save(new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItems));
        orderDao.save(new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItems));

        // when
        List<Order> actual = orderService.list();

        // then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Nested
    class ChangeOrderStatus {

        @DisplayName("성공")
        @Test
        void success() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(null, 2, false));
            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹1"));
            Menu menu = menuDao.save(new Menu("메뉴1", BigDecimal.valueOf(1000), menuGroup.getId(), new ArrayList<>()));
            ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
            orderLineItems.add(orderLineItemDao.save(new OrderLineItem(menu.getId(), 3)));
            Order order = orderDao.save(
                    new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems));

            // when
            Order changedOrder = orderService.changeOrderStatus(order.getId(),
                    new Order(orderTable.getId(), OrderStatus.MEAL.name(),
                            LocalDateTime.now(), orderLineItems));

            // then
            assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }

        @DisplayName("주문을 찾지 못하면 예외를 발생시킨다.")
        @Test
        void notFoundOrder_exception() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(null, 2, false));
            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹1"));
            Menu menu = menuDao.save(new Menu("메뉴1", BigDecimal.valueOf(1000), menuGroup.getId(), new ArrayList<>()));
            ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
            orderLineItems.add(orderLineItemDao.save(new OrderLineItem(menu.getId(), 3)));

            // when
            assertThatThrownBy(() -> orderService.changeOrderStatus(0L,
                    new Order(orderTable.getId(), OrderStatus.MEAL.name(),
                            LocalDateTime.now(), orderLineItems)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 상태가 완료이면 예외를 발생시킨다.")
        @Test
        void orderStatusIsCompletion_exception() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(null, 2, false));
            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹1"));
            Menu menu = menuDao.save(new Menu("메뉴1", BigDecimal.valueOf(1000), menuGroup.getId(), new ArrayList<>()));
            ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
            orderLineItems.add(orderLineItemDao.save(new OrderLineItem(menu.getId(), 3)));
            Order order = orderDao.save(
                    new Order(orderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems));

            // when
            assertThatThrownBy(() -> orderService.changeOrderStatus(0L,
                    new Order(orderTable.getId(), OrderStatus.MEAL.name(),
                            LocalDateTime.now(), orderLineItems)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
