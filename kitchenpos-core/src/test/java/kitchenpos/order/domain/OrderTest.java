package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.exception.InvalidOrderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class OrderTest {

    private OrderValidator orderValidator;

    @BeforeEach
    void setUp() {
        orderValidator = Mockito.mock(OrderValidator.class);
    }

    @Nested
    @DisplayName("주문을 생성할 때 ")
    class CreateTest {

        @Test
        @DisplayName("메뉴가 없으면 예외가 발생한다.")
        void menuNotExistsFailed() {
            doThrow(IllegalArgumentException.class).when(orderValidator)
                    .validate(anyLong(), eq(Collections.emptyList()));

            assertThatThrownBy(() -> Order.create(1L, Collections.emptyList(), orderValidator))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블이 없으면 예외가 발생한다.")
        void orderTableNotExistsFailed() {
            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 1), new OrderLineItem(2L, 1));
            doThrow(IllegalArgumentException.class).when(orderValidator).validate(eq(1L), anyList());

            assertThatThrownBy(() -> Order.create(1L, orderLineItems, orderValidator))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블이 비어있으면 예외가 발생한다.")
        void orderTableEmptyFailed() {
            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 1), new OrderLineItem(2L, 1));
            doThrow(IllegalArgumentException.class).when(orderValidator).validate(eq(null), anyList());

            assertThatThrownBy(() -> Order.create(null, orderLineItems, orderValidator))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("주문 상태를 변경할 때 ")
    class ChangeStatusTest {

        @Test
        @DisplayName("식사가 이미 완료된 경우 예외가 발생한다.")
        void alreadyCompletedFailed() {
            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 1), new OrderLineItem(2L, 1));
            Order order = new Order(1L, 1L, OrderStatus.COMPLETION, LocalDateTime.now(), orderLineItems);

            assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COMPLETION))
                    .isInstanceOf(InvalidOrderException.class);
        }

        @Test
        @DisplayName("정상적인 경우 상태 변경에 성공한다.")
        void changeStatus() {
            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 1), new OrderLineItem(2L, 1));
            doNothing().when(orderValidator).validate(anyLong(), anyList());
            Order order = Order.create(1L, orderLineItems, orderValidator);
            order.changeOrderStatus(OrderStatus.COMPLETION);

            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
        }
    }
}
