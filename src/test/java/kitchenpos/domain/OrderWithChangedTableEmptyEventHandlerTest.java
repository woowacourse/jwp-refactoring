package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderWithChangedTableEmptyEventHandler;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableEmptyChangedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderWithChangedTableEmptyEventHandlerTest {

    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderWithChangedTableEmptyEventHandler orderWithChangedTableEmptyEventHandler;

    @DisplayName("조리나 식사 상태인 주문 테이블의 빈 상태를 수정할 경우 예외 처리")
    @Test
    void validate() {
        when(orderRepository.findAllByOrderTableId(any())).thenReturn(
            Collections.singletonList(new Order(1L, 1L, OrderStatus.COOKING)));

        assertThatIllegalArgumentException().isThrownBy(
            () -> orderWithChangedTableEmptyEventHandler.handle(
                new OrderTableEmptyChangedEvent(new OrderTable(1L, null, 4, false))
            )
        );
    }
}
