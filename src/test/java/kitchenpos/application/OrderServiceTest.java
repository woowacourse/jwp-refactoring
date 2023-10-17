package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    private List<OrderLineItem> orderLineItems;
    private OrderTable orderTable;

    @BeforeEach
    void beforeEach() {
        MenuGroup menuGroup = new MenuGroup("menu group");
        menuGroup = testFixtureBuilder.buildMenuGroup(menuGroup);

        Menu menu1 = new Menu("name", new BigDecimal( 100), menuGroup.getId(), Collections.emptyList());
        menu1 = testFixtureBuilder.buildMenu(menu1);
        Menu menu2 = new Menu("name", new BigDecimal( 100), menuGroup.getId(), Collections.emptyList());
        menu2 = testFixtureBuilder.buildMenu(menu2);

        orderLineItems = new ArrayList<>();
        final OrderLineItem orderLineItem1 = new OrderLineItem(null, menu1, 1L);
        final OrderLineItem orderLineItem2 = new OrderLineItem(null, menu2, 1L);
        orderLineItems.add(orderLineItem1);
        orderLineItems.add(orderLineItem2);

        orderTable = new OrderTable(null, 3, false);
        orderTable = testFixtureBuilder.buildOrderTable(orderTable);
    }

    @DisplayName("주문 생성 테스트")
    @Nested
    class OrderCreateTest {

        @DisplayName("주문을 생성한다.")
        @Test
        void orderCreate() {
            //given
            final Order order = new Order(orderTable, null, null, orderLineItems);

            //when
            final Order actual = orderService.create(order);

            //then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isNotNull();
            });
        }

        @DisplayName("주문 항목이 비어있으면 실패한다.")
        @Test
        void orderCreateFailWhenOrderLineItemsIsEmpty() {
            //given
            final Order order = new Order(orderTable, null, null, Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문할 메뉴가 존재하지 않으면 실패한다.")
        @Test
        void orderCreateFailWhenNotExistMenu() {
            //given
            final Order order = new Order(orderTable, null, null, orderLineItems);

            final Menu notExistMenu = new Menu("menu", new BigDecimal(1000), null, Collections.emptyList());
            final OrderLineItem orderLineItem = new OrderLineItem(null, notExistMenu, 1);
            orderLineItems.add(orderLineItem);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 존재하지 않으면 실패한다.")
        @Test
        void orderCreateFailWhenNotExistOrderTable() {
            //given
            final OrderTable notExistOrderTable = new OrderTable(null, 3, true);
            final Order order = new Order(notExistOrderTable, null, null, orderLineItems);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 비어있으면 실패한다.")
        @Test
        void orderCreateFailWhenOrderTableIsEmpty() {
            //given
            orderTable = new OrderTable(null, 3, true);
            orderTable = testFixtureBuilder.buildOrderTable(orderTable);

            final Order order = new Order(orderTable, null, null, orderLineItems);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 조회 테스트")
    @Nested
    class OrderFindTest {

        @DisplayName("주문을 전체 조회한다.")
        @Test
        void orderFindAll() {
            //given
            Order order = new Order(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.emptyList());
            order = testFixtureBuilder.buildOrder(order);

            //when
            final List<Order> actual = orderService.list();

            //then
            final Long orderId = order.getId();
            assertSoftly(softly -> {
                softly.assertThat(actual.size()).isEqualTo(1);
                softly.assertThat(actual.get(0).getId()).isEqualTo(orderId);
            });
        }
    }

    @DisplayName("주문 상태 변경 테스트")
    @Nested
    class OrderStatusChangeTest {

        @DisplayName("주문 상태를 변경한다.")
        @Test
        void orderStatusChange() {
            //given
            Order order = new Order(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.emptyList());
            order = testFixtureBuilder.buildOrder(order);

            final Order changeStatus = new Order(null, OrderStatus.MEAL.name(), null, null);

            //when
            final Order actual = orderService.changeOrderStatus(order.getId(), changeStatus);

            //then
            final Long orderId = order.getId();
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isEqualTo(orderId);
                softly.assertThat(actual.getOrderStatus()).isEqualTo(changeStatus.getOrderStatus());
            });
        }

        @DisplayName("존재하지 않는 주문은 변경할 수 없다.")
        @Test
        void orderStatusChangeFailWhenNotExistOrder() {
            //given
            Order order = new Order(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.emptyList());

            final Order changeStatus = new Order(null, OrderStatus.MEAL.name(), null, null);
            changeStatus.setOrderStatus(OrderStatus.MEAL.name());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeStatus))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 상태가 완료면 변경할 수 없다.")
        @Test
        void orderStatusChangeFailWhenStatusIsCompletion() {
            //given
            Order completionOrder = new Order(orderTable, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Collections.emptyList());
            completionOrder = testFixtureBuilder.buildOrder(completionOrder);

            final Order changeStatus = new Order(null, OrderStatus.MEAL.name(), null, null);

            // when & then
            final Long completionOrderId = completionOrder.getId();
            assertThatThrownBy(() -> orderService.changeOrderStatus(completionOrderId, changeStatus))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
