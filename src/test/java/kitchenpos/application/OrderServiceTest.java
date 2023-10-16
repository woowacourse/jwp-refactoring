package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("menu group");
        menuGroup = testFixtureBuilder.buildMenuGroup(menuGroup);

        Menu menu1 = new Menu();
        menu1.setPrice(new BigDecimal(100));
        menu1.setMenuGroupId(menuGroup.getId());
        menu1.setName("menu");
        menu1.setMenuProducts(Collections.emptyList());
        menu1 = testFixtureBuilder.buildMenu(menu1);
        Menu menu2 = new Menu();
        menu2.setPrice(new BigDecimal(100));
        menu2.setMenuGroupId(menuGroup.getId());
        menu2.setName("menu");
        menu2.setMenuProducts(Collections.emptyList());
        menu2 = testFixtureBuilder.buildMenu(menu2);

        orderLineItems = new ArrayList<>();
        final OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setMenuId(menu1.getId());
        orderLineItem1.setQuantity(1L);
        orderLineItem1.setSeq(1L);
        final OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setMenuId(menu2.getId());
        orderLineItem2.setQuantity(1L);
        orderLineItem2.setSeq(1L);
        orderLineItems.add(orderLineItem1);
        orderLineItems.add(orderLineItem2);

        orderTable = new OrderTable();
        orderTable.setTableGroupId(null);
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(3);
        orderTable = testFixtureBuilder.buildOrderTable(orderTable);
    }

    @DisplayName("주문 생성 테스트")
    @Nested
    class OrderCreateTest {

        @DisplayName("주문을 생성한다.")
        @Test
        void orderCreate() {
            //given
            final Order order = new Order();
            order.setOrderTableId(orderTable.getId());
            order.setOrderLineItems(orderLineItems);

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
            final Order order = new Order();
            order.setOrderTableId(orderTable.getId());
            order.setOrderLineItems(Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문할 메뉴가 존재하지 않으면 실패한다.")
        @Test
        void orderCreateFailWhenNotExistMenu() {
            //given
            final Order order = new Order();
            order.setOrderTableId(orderTable.getId());

            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(-1L);
            orderLineItems.add(orderLineItem);
            order.setOrderLineItems(orderLineItems);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 존재하지 않으면 실패한다.")
        @Test
        void orderCreateFailWhenNotExistOrderTable() {
            //given
            final Order order = new Order();
            order.setOrderTableId(-1L);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 비어있으면 실패한다.")
        @Test
        void orderCreateFailWhenOrderTableIsEmpty() {
            //given
            orderTable = new OrderTable();
            orderTable.setTableGroupId(null);
            orderTable.setEmpty(true);
            orderTable.setNumberOfGuests(3);
            orderTable = testFixtureBuilder.buildOrderTable(orderTable);

            final Order order = new Order();
            order.setOrderTableId(orderTable.getId());

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
            Order order = new Order();
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderStatus(OrderStatus.COOKING.name());
            order.setOrderTableId(orderTable.getId());
            order.setOrderLineItems(Collections.emptyList());
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
            Order order = new Order();
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderStatus(OrderStatus.COOKING.name());
            order.setOrderTableId(orderTable.getId());
            order.setOrderLineItems(Collections.emptyList());
            order = testFixtureBuilder.buildOrder(order);

            final Order changeStatus = new Order();
            changeStatus.setOrderStatus(OrderStatus.MEAL.name());

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
            final Order order = new Order();
            order.setId(-1L);

            final Order changeStatus = new Order();
            changeStatus.setOrderStatus(OrderStatus.MEAL.name());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeStatus))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 상태가 완료면 변경할 수 없다.")
        @Test
        void orderStatusChangeFailWhenStatusIsCompletion() {
            //given
            Order completionOrder = new Order();
            completionOrder.setOrderStatus(OrderStatus.COMPLETION.name());
            completionOrder.setOrderedTime(LocalDateTime.now());
            completionOrder.setOrderTableId(orderTable.getId());
            completionOrder.setOrderLineItems(orderLineItems);
            completionOrder = testFixtureBuilder.buildOrder(completionOrder);

            final Order changeStatus = new Order();
            changeStatus.setOrderStatus(OrderStatus.MEAL.name());

            // when & then
            final Long completionOrderId = completionOrder.getId();
            assertThatThrownBy(() -> orderService.changeOrderStatus(completionOrderId, changeStatus))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
