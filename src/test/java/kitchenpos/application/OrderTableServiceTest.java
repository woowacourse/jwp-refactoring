package kitchenpos.application;

import kitchenpos.event.OrderTableUngroupEventPublisher;
import kitchenpos.order.application.OrderTableService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.application.ServiceTest.DomainFactory.CREATE_ORDER_TABLE;
import static kitchenpos.application.ServiceTest.DomainFactory.CREATE_TABLE_GROUP;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

    @Mock
    private OrderTableUngroupEventPublisher orderTableUngroupEventPublisher;

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

    // TODO 그룹 해제 테스트를 통합 테스트로 추가하든 EventListener만 따로 테스트하기
}
