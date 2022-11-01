package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {

    private OrderValidator orderValidator;

    @BeforeEach
    void setUp() {
        orderValidator = mock(OrderValidator.class);

        doNothing().when(orderValidator).validate(anyLong(), any(OrderLineItems.class));
    }

    @Test
    @DisplayName("주문을 생성하면 조리 상태가 된다.")
    void create() {
        OrderLineItems orderLineItems = new OrderLineItems(List.of(new OrderLineItem(1L, 2)));
        Order order = Order.startCooking(1L, orderLineItems, orderValidator);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    @DisplayName("계산 완료 상태로 변경하면 예외가 발생한다.")
    void changeStatusIsCompletion() {
        OrderLineItems orderLineItems = new OrderLineItems(List.of(new OrderLineItem(1L, 2)));
        Order order = Order.startCooking(1L, orderLineItems, orderValidator);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> order.changeStatus(OrderStatus.COMPLETION))
                .withMessage("계산 완료된 주문입니다.");
    }
}
