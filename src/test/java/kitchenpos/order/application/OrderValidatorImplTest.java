package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.exception.InvalidOrderStatusException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.support.fixture.OrderFixture;
import kitchenpos.support.fixture.OrderLineItemFixture;
import kitchenpos.support.fixture.OrderTableFixture;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderValidatorImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderValidatorImpl orderValidator;

    @Test
    @DisplayName("주문이 완료됐으면 예외가 발생한다.")
    void validateOrderNotCompleted() {
        // given
        final Order order = createNotCompletedOrder();
        given(orderRepository.findByOrderTableId(1L))
                .willReturn(Optional.of(order));

        // when, then
        assertThatThrownBy(() -> orderValidator.validateOrderNotCompleted(1L))
                .isExactlyInstanceOf(InvalidOrderStatusException.class);
    }

    private static Order createNotCompletedOrder() {
        final OrderLineItem orderLineItem1 = OrderLineItemFixture.create(1L);
        final OrderLineItem orderLineItem2 = OrderLineItemFixture.create(2L);
        final OrderTable orderTable = OrderTableFixture.create(false, 10);
        return OrderFixture.create(orderTable.getId(), OrderStatus.COOKING, orderLineItem1, orderLineItem2);
    }

    @Test
    @DisplayName("주문이 완료되지 않은 테이블 ID가 존재하면 예외가 발생한다.")
    void validateAnyOrdersNotCompleted() {
        // given
        final Order order1 = createNotCompletedOrder();
        final Order order2 = createNotCompletedOrder();
        final List<Long> orderTableIds = Arrays.asList(1L, 2L);
        given(orderRepository.findAllByOrderTableId(orderTableIds))
                .willReturn(Arrays.asList(order1, order2));

        // when, then
        assertThatThrownBy(() -> orderValidator.validateAnyOrdersNotCompleted(orderTableIds))
                .isExactlyInstanceOf(InvalidOrderStatusException.class);
    }
}
