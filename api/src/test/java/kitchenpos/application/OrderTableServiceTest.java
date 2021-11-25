package kitchenpos.application;

import kitchenpos.application.table.OrderTableService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.repository.OrderTableRepository;
import kitchenpos.event.OrderStatusCheckEventPublisher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ServiceTest
@DisplayName("OrderTable 서비스 테스트")
public class OrderTableServiceTest {
    @InjectMocks
    private OrderTableService orderTableService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderStatusCheckEventPublisher orderStatusCheckEventPublisher;

    @DisplayName("주문 아이디에 해당하는 모든 주문 테이블을 조회한다. - 실패, 조회 결과와 요청의 크기가 다름.")
    @Test
    void findAllOrderTables() {
        // given
        List<Long> orderTableIds = Arrays.asList(1L, 1L, 2L);
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(10, false),
                new OrderTable(20, false)
        );
        given(orderTableRepository.findAllByIdIn(orderTableIds))
                .willReturn(orderTables);

        // when - then
        assertThatThrownBy(() -> orderTableService.findAllOrderTables(orderTableIds))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(times(1))
                .findAllByIdIn(orderTableIds);
    }
}
