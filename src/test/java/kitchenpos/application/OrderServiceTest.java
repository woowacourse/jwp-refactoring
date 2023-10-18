package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    private OrderTable orderTable;

    private Menu menu;

    @BeforeEach
    void setUp() {
        OrderTable newOrderTable = createOrderTable(false, null, 10);
        orderTable = tableDao.save(newOrderTable);

        Menu newMenu = Menu.of("치킨", 15_000L, 1L);
        menu = menuDao.save(newMenu);
    }

    @Nested
    class 주문_생성 {

        @Test
        void 정상_요청() {
            // given
            OrderLineItem orderLineItem = createOrderLineItem(null, 1, menu.getId());
            Order order = createOrder(orderTable.getId(), COOKING, orderLineItem);

            // when
            Order savedOrder = orderService.create(order);

            // then
            assertSoftly(
                    softly -> {
                        softly.assertThat(savedOrder.getId()).isNotNull();
                        softly.assertThat(savedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
                        softly.assertThat(savedOrder.getOrderLineItems().get(0))
                                .usingRecursiveComparison()
                                .ignoringFields("seq")
                                .isEqualTo(orderLineItem);
                    }
            );
        }

        @Test
        void 주문_상품이_1개_미만이면_예외_발생() {
            // given
            Order order = createOrder(orderTable.getId(), COOKING);

            // when, then
            assertThatThrownBy(
                    () -> orderService.create(order)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_아이템의_메뉴가_존재하지_않으면_예외_발생() {
            // given
            Long invalidMenuId = -1L;
            OrderLineItem orderLineItem = createOrderLineItem(null, 1, invalidMenuId);
            Order order = createOrder(orderTable.getId(), COOKING, orderLineItem);

            // when, then
            assertThatThrownBy(
                    () -> orderService.create(order)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_존재하지_않으면_예외_발생() {
            // given
            OrderLineItem orderLineItem = createOrderLineItem(null, 1, menu.getId());
            long invalidOrderTableId = -1L;
            Order order = createOrder(invalidOrderTableId, COOKING, orderLineItem);

            // when, then
            assertThatThrownBy(
                    () -> orderService.create(order)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_비어_있으면_예외_발생() {
            // given
            OrderTable newOrderTable = createOrderTable(true, null, 10);
            OrderTable orderTable = tableDao.save(newOrderTable);
            OrderLineItem orderLineItem = createOrderLineItem(null, 1, menu.getId());
            Order order = createOrder(orderTable.getId(), COOKING, orderLineItem);

            // when, then
            assertThatThrownBy(
                    () -> orderService.create(order)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 전체_주문_조회 {

        @Test
        void 정상_요청() {
            // given
            OrderLineItem orderLineItem = createOrderLineItem(null, 1, menu.getId());
            Order order = createOrder(orderTable.getId(), COOKING, orderLineItem);
            Order savedOrder = orderService.create(order);

            // when
            List<Order> orders = orderService.readAll();

            // then
            assertThat(orders)
                    .extracting(Order::getId)
                    .contains(savedOrder.getId());
        }
    }

    @Nested
    class 주문_상태_변경 {

        @Test
        void 정상_요청() {
            // given
            OrderLineItem orderLineItem = createOrderLineItem(null, 1, menu.getId());
            Order order = createOrder(orderTable.getId(), COOKING, orderLineItem);
            Order savedOrder = orderService.create(order);

            Order newOrder = createOrder(orderTable.getId(), MEAL, orderLineItem);

            // when
            Order updatedOrder = orderService.changeOrderStatus(savedOrder.getId(), newOrder);

            // then
            assertThat(updatedOrder.getOrderStatus()).isEqualTo(newOrder.getOrderStatus());
        }

        @Test
        void 주문이_존재하지_않으면_예외_발생() {
            // given
            Long invalidOrderId = -1L;

            Order newOrder = createOrder(orderTable.getId(), MEAL);

            // when, then
            assertThatThrownBy(
                    () -> orderService.changeOrderStatus(invalidOrderId, newOrder)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문이_상태가_COMPLETION이면_예외_발생() {
            // given
            OrderLineItem orderLineItem = createOrderLineItem(null, 1, menu.getId());
            Order order = createOrder(orderTable.getId(), COMPLETION, orderLineItem);
            Order savedOrder = orderDao.save(order);

            Order newOrder = createOrder(orderTable.getId(), MEAL, orderLineItem);

            // when, then
            assertThatThrownBy(
                    () -> orderService.changeOrderStatus(savedOrder.getId(), newOrder)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    private OrderTable createOrderTable(final boolean empty,
                                        final Long tableGroupId,
                                        final Integer numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    private Order createOrder(final Long orderTableId,
                              final OrderStatus status,
                              final OrderLineItem... orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(status.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(Arrays.asList(orderLineItems));
        return order;
    }

    private OrderLineItem createOrderLineItem(final Long orderId,
                                              final Integer quantity,
                                              final Long menuId) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setQuantity(quantity);
        orderLineItem.setMenuId(menuId);
        return orderLineItem;
    }
}
