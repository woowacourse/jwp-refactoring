package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @DisplayName("객체 생성: 객체가 정상적으로 생성된다.")
    @Test
    void create() {
        // given
        Long id = 1L;
        Long orderTableId = 2L;
        String orderStatus = OrderStatus.COOKING.name();
        LocalDateTime orderedTime = LocalDateTime.now();
        List<OrderLineItem> orderLineItems = Collections.singletonList(new OrderLineItem(1L, null, 1L, 1L));

        // when
        Order order = new Order(id, orderTableId, orderStatus, orderedTime, new OrderLineItems(orderLineItems));

        // then
        assertThat(order.getId()).isEqualTo(1L);
        assertThat(order.getOrderTableId()).isEqualTo(2L);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(order.getOrderedTime()).isEqualTo(orderedTime);
    }

    @DisplayName("id를 null로 변경한다.")
    @Test
    void initId() {
        // given
        Long id = 1L;
        Long orderTableId = 2L;
        String orderStatus = OrderStatus.COOKING.name();
        LocalDateTime orderedTime = LocalDateTime.now();
        List<OrderLineItem> orderLineItems = Collections.singletonList(new OrderLineItem(1L, null, 1L, 1L));
        Order order = new Order(id, orderTableId, orderStatus, orderedTime, new OrderLineItems(orderLineItems));

        // when
        order.initId();

        // then
        assertThat(order.getId()).isNull();
    }

    @DisplayName("주문 시작 초기화 : 주문 최초 등록 시 상태를 초기화한다.")
    @Test
    void startOrder() {
        // given
        Long id = 1L;
        Order order = new Order(id, null, null, null, null);
        OrderValidator orderValidator = new OrderValidatorTestImpl();

        // when
        order.startOrder(orderValidator);

        // then
        assertThat(order.getOrderedTime()).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("changeOrderStatus(): 주문의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        Long id = 1L;
        Order order = new Order(id, null, null, null, null);
        OrderValidator orderValidator = new OrderValidatorTestImpl();

        // when then
        order.changeOrderStatus(orderValidator, OrderStatus.COOKING.name());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());

        order.changeOrderStatus(orderValidator, OrderStatus.MEAL.name());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());

        order.changeOrderStatus(orderValidator, OrderStatus.COMPLETION.name());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }
}