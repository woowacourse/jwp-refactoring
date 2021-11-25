package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.order.exception.CannotChangeOrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("Order 도메인 테스트")
class OrderTest {

//    @DisplayName("[실패] 빈 orderLineItmes면 Order 생성 불가")
//    @Test
//    void newOrder_emptyOrderLineItems_ExceptionThrown() {
//        // given
//        List<OrderLineItem> orderLineItems = Collections.emptyList();
//
//        // when
//        // then
//        assertThatThrownBy(() -> new Order(1L, orderLineItems))
//            .isInstanceOf(InvalidOrderException.class);
//    }

//    @DisplayName("[실패] OrderTable이 empty 상태라면 생성 불가") //TODO Validator 테스트 코드 구현
//    @Test
//    void newOrder_emptyOrderTable_ExceptionThrown() {
//        // given
//        List<OrderLineItem> orderLineItems = Collections
//            .singletonList(new OrderLineItem(1L, 1L));
//
//        // when
//        // then
//        assertThatThrownBy(() -> new Order(1L, orderLineItems))
//            .isInstanceOf(InvalidOrderException.class);
//    }

    @DisplayName("[성공] OrderStatus가 COMPLETION가 아니라면 OrderStatus 변경")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"MEAL", "COMPLETION"})
    void changeOrderStatus_ValidOrderStatus_ExceptionThrown(OrderStatus orderStatus) {
        // given
        List<OrderLineItem> orderLineItems = Collections
            .singletonList(new OrderLineItem(1L, 1L));
        Order order = new Order(1L, orderLineItems);

        // when
        order.changeOrderStatus(orderStatus.name());

        // then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus.name());
    }

    @DisplayName("[실패] OrderStatus가 COMPLETION이라면 OrderStatus 변경 불가")
    @Test
    void changeOrderStatus_OrderStatusCompletion_ExceptionThrown() {
        // given
        List<OrderLineItem> orderLineItems = Collections
            .singletonList(new OrderLineItem(1L, 1L));
        Order order = new Order(1L, orderLineItems);

        order.changeOrderStatus(OrderStatus.COMPLETION.name());

        // when
        // then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL.name()))
            .isInstanceOf(CannotChangeOrderStatus.class);

    }
}
