package kitchenpos.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FindOrderTableInOrderStatusServiceTest {

    @Test
    @DisplayName("테이블 id와 주문 상태 목록 조건으로 주문이 있는지 확인한다.")
    void existByOrderStatus() {
        final OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(true);
        final FindOrderTableInOrderStatusService findOrderTableInOrderStatusService
                = new FindOrderTableInOrderStatusService(orderRepository);

        final boolean existTrue = findOrderTableInOrderStatusService.existByOrderStatus(
                1L,
                List.of(OrderStatus.COOKING, OrderStatus.MEAL)
        );

        assertThat(existTrue).isTrue();
    }
}