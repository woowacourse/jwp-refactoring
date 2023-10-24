package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    private static final OrderTable NOT_EMPTY_ORDER_TABLE = new OrderTable(3, false);
    private static final Menu MENU = new Menu("menu", Price.from(BigDecimal.valueOf(1000L)), new MenuGroup("menuGroup"));

    @DisplayName("주문을 처음 생성할 때, 주문 상태가 COOKING 으로 생성된다.")
    @Test
    void createDefault() {
        // given
        // when
        final Order order = Order.createDefault(NOT_EMPTY_ORDER_TABLE, LocalDateTime.now());

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("주문을 처음 생성할 때, 주문 테이블이 비어있다면 예외가 발생한다.")
    @Test
    void createDefault_fail() {
        // given
        final OrderTable orderTable = new OrderTable(3, true);

        // when, then
        assertThatThrownBy(() -> Order.createDefault(orderTable, LocalDateTime.now()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문하려는 테이블은 비어있을 수 없습니다.");
    }

    @DisplayName("주문에 주문 메뉴를 추가한다.")
    @Test
    void addOrderLineItems() {
        // given
        final Order order = Order.createDefault(NOT_EMPTY_ORDER_TABLE, LocalDateTime.now());
        final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(order, MENU, 2));

        // when
        order.addOrderLineItems(orderLineItems);

        // then
        assertThat(order.getOrderLineItems()).hasSize(1);
    }

    @DisplayName("주문에 주문 메뉴를 추가할 때, 메뉴가 비어있으면 예외가 발생한다.")
    @Test
    void addOrderLineItems_fail() {
        // given
        final Order order = Order.createDefault(NOT_EMPTY_ORDER_TABLE, LocalDateTime.now());

        // when, then
        assertThatThrownBy(() -> order.addOrderLineItems(Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문 메뉴가 비어있을 수 없습니다.");
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        final Order order = Order.createDefault(NOT_EMPTY_ORDER_TABLE, LocalDateTime.now());

        // when
        order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태를 변경할 때, 이미 완료된 주문이라면 예외가 발생한다.")
    @Test
    void changeOrderStatus_fail() {
        // given
        final Order order = Order.createDefault(NOT_EMPTY_ORDER_TABLE, LocalDateTime.now());
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // when, then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 완료된 주문이라면 주문 상태를 변경할 수 없습니다.");
    }
}
