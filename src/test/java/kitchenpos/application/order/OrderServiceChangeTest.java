package kitchenpos.application.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderServiceChangeTest extends OrderServiceTest {

    @DisplayName("주문 상태 변경 시 주문 번호가 등록이 되어 있어야 한다.")
    @Test
    void nullOrderStatus() {
        //given
        given(orderDao.findById(BASIC_ORDER_ID)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(BASIC_ORDER_ID, standardOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 시 완료된 주문은 아니어야 한다.")
    @Test
    void completedOrderStatus() {
        //given
        standardOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(BASIC_ORDER_ID)).willReturn(Optional.of(standardOrder));

        //when

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(BASIC_ORDER_ID, standardOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        //given
        standardOrder.setOrderStatus(OrderStatus.MEAL.name());
        given(orderDao.findById(BASIC_ORDER_ID)).willReturn(Optional.of(standardOrder));
        given(orderLineItemDao.findAllByOrderId(BASIC_ORDER_ID)).willReturn(standardOrderLineItems);

        //when
        Order order = orderService.changeOrderStatus(BASIC_ORDER_ID, standardOrder);

        //then
        assertAll(
            () -> assertThat(order.getId()).isEqualTo(BASIC_ORDER_ID),
            () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name()),
            () -> assertThat(order.getOrderLineItems().size()).isEqualTo(BASIC_SIZE),
            () -> assertThat(order.getOrderTableId()).isEqualTo(BASIC_ORDER_TABLE_ID)
        );
    }

}
