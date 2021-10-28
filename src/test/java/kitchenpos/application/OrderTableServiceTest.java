package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("OrderTable 서비스 테스트")
public class OrderTableServiceTest extends ServiceTest {
    @InjectMocks
    private OrderTableService orderTableService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

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

    @DisplayName("그룹을 해제한다. - 실패, 조리 혹은 식사 중인 주문이 존재함.")
    @Test
    void ungroup() {
        // given
        Long tableGroupId = 1L;
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(1L, new TableGroup(1L, LocalDateTime.now()), 10, false),
                new OrderTable(2L, new TableGroup(1L, LocalDateTime.now()), 20, false)
        );
        List<Orders> orders = orderTables.stream()
                .map(Orders::new)
                .collect(Collectors.toList());

        given(orderTableRepository.findAllByTableGroupId(tableGroupId)).willReturn(orderTables);
        given(orderRepository.findAllByOrderTableId(anyLong())).willReturn(orders);

        // when -  then
        assertThatThrownBy(() -> orderTableService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(times(1))
                .findAllByTableGroupId(tableGroupId);
        then(orderRepository).should(times(1))
                .findAllByOrderTableId(anyLong());
    }
}
