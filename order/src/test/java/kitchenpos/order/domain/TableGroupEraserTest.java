package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 그룹지정 해제시, order, orderTable 유효성 검사후 ungroup 한다.")
@ExtendWith(MockitoExtension.class)
class TableGroupEraserTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableGroupEraser tableGroupEraser;

    @DisplayName("단체 지정의 주문 테이블의 주문이 있고, 주문 상태가 `COOKING`, `MEAL`이라면 예외가 발생한다")
    @Test
    void ungroupExceptionExistsAndStatus() {
        OrderTable orderTable = OrderTable.builder()
                .empty(true)
                .build();
        OrderTable savedOrderTable1 = OrderTable.builder()
                .of(orderTable)
                .id(1L)
                .build();
        OrderTable savedOrderTable2 = OrderTable.builder()
                .of(orderTable)
                .id(2L)
                .build();
        final List<OrderTable> orderTables = Arrays.asList(savedOrderTable1, savedOrderTable2);
        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(orderTables);
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> tableGroupEraser.ungroup(any())).isInstanceOf(IllegalArgumentException.class);
    }

}
