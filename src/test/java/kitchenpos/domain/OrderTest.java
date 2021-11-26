package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @DisplayName("객체 생성: 객체가 정상적으로 생성된다.")
    @Test
    void create() {
        // given
        Long id = 1L;
        OrderTable orderTable = new OrderTable(1L, null, 4, false);
        String orderStatus = OrderStatus.COOKING.name();
        LocalDateTime orderedTime = LocalDateTime.now();
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        // when
        Order order = new Order(id, orderTable, orderStatus, orderedTime, orderLineItems);

        // then
        assertThat(order.getId()).isEqualTo(id);
        assertThat(order.getOrderTable()).isEqualTo(orderTable);
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
        assertThat(order.getOrderedTime()).isEqualTo(orderedTime);
    }

    @DisplayName("id를 null로 변경한다.")
    @Test
    void initId() {
        // given
        Long id = 1L;
        OrderTable orderTable = new OrderTable(1L, null, 4, false);
        String orderStatus = OrderStatus.COOKING.name();
        LocalDateTime orderedTime = LocalDateTime.now();
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        Order order = new Order(id, orderTable, orderStatus, orderedTime, orderLineItems);

        // when
        order.initId();

        // then
        assertThat(order.getId()).isNull();
    }

    @DisplayName("주문 최초 등록 시 상태를 초기화한다.")
    @Test
    void initOrderStart() {
        // given
        Long id = 1L;
        OrderTable orderTable = new OrderTable(1L, null, 4, false);
        Order order = new Order(id, null, null, null, null);

        // when
        order.initOrderStart(orderTable);

        // then
        assertThat(order.getOrderTable()).isNotNull();
        assertThat(order.getOrderedTime()).isNotNull();
        assertThat(order.getOrderStatus()).isNotNull();
    }

    @DisplayName("changeOrderStatus(): 주문의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        Long id = 1L;
        Order order = new Order(id, null, null, null, null);

        // when then
        order.changeOrderStatus(OrderStatus.COOKING.name());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());

        order.changeOrderStatus(OrderStatus.MEAL.name());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());

        order.changeOrderStatus(OrderStatus.COMPLETION.name());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("validateChangeOrderStatus(): 주문의 상태가 COMPLETION이라면 상태를 변경할 수 없다.")
    @Test
    void validateChangeOrderStatus() {
        // given
        Order order = new Order(1L, null, OrderStatus.COMPLETION.name(), null, null);

        // when then
        assertThatThrownBy(order::validateChangeOrderStatus)
                .isInstanceOf(IllegalArgumentException.class);
    }
}