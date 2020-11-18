package kitchenpos.application;

import kitchenpos.domain.order.*;
import kitchenpos.dto.order.OrderTableRequest;
import kitchenpos.dto.order.TableGroupCreateRequest;
import kitchenpos.dto.order.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    private static final Long 테이블_ID_1 = 1L;
    private static final Long 테이블_ID_2 = 2L;
    private static final Integer 테이블_사람_1명 = 1;
    private static final Integer 테이블_사람_2명 = 2;
    private static final Boolean 테이블_비어있음 = true;
    private static final Boolean 테이블_비어있지않음 = false;
    private static final Long 테이블_그룹_ID_1 = 1L;
    private static final LocalDateTime 테이블_그룹_생성시간 = LocalDateTime.now();
    List<OrderTableRequest> orderTableRequests;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    private TableGroupService tableGroupService;
    @Mock
    private TableGroupRepository tableGroupRepository;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderTableRepository, tableGroupRepository, orderRepository);
        orderTableRequests = Arrays.asList(
                new OrderTableRequest(테이블_ID_1),
                new OrderTableRequest(테이블_ID_2)
        );
    }

    @DisplayName("TableGroup 생성이 올바르게 수행된다.")
    @Test
    void createTest() {
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있음, null),
                new OrderTable(테이블_ID_2, 테이블_사람_2명, 테이블_비어있음, null)
        );
        TableGroup tableGroup = new TableGroup(orderTables);
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableRequests);
        when(orderTableRepository.findAllById(anyList())).thenReturn(orderTables);
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(null);
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(tableGroup);

        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupCreateRequest);

        assertThat(tableGroupResponse.getOrderTables())
                .hasSize(2)
                .extracting("id")
                .containsOnly(테이블_ID_1, 테이블_ID_2);
    }

    @DisplayName("예외 테스트 : TableGroup 생성 중 빈 테이블 목록을 보내면, 예외가 발생한다.")
    @Test
    void createWithEmptyTablesExceptionTest() {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(Collections.emptyList());
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화하려는 테이블의 상태가 유효하지 않습니다.");
    }

    @DisplayName("예외 테스트 : TableGroup 생성 중 2개 미만의 테이블 목록을 보내면, 예외가 발생한다.")
    @Test
    void createWithLowTablesExceptionTest() {
        List<OrderTableRequest> orderTableRequests = Arrays.asList(new OrderTableRequest(테이블_ID_1));
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableRequests);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화하려는 테이블의 상태가 유효하지 않습니다.");
    }

    @DisplayName("예외 테스트 : TableGroup 생성 중 DB에 없는 테이블 목록을 보내면, 예외가 발생한다.")
    @Test
    void createWithDBNotFoundTablesExceptionTest() {
        List<OrderTable> invalidOrderTables = Arrays.asList(
                new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있음, null)
        );
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableRequests);
        when(orderTableRepository.findAllById(anyList())).thenReturn(invalidOrderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화하려는 테이블의 상태가 유효하지 않습니다.");
    }

    @DisplayName("예외 테스트 : TableGroup 생성 중 비어있지 않은 테이블 목록을 보내면, 예외가 발생한다.")
    @Test
    void createWithNotEmptyTablesExceptionTest() {
        List<OrderTable> invalidOrderTables = Arrays.asList(
                new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있지않음, null),
                new OrderTable(테이블_ID_2, 테이블_사람_2명, 테이블_비어있지않음, null)
        );
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableRequests);
        when(orderTableRepository.findAllById(anyList())).thenReturn(invalidOrderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화하려는 테이블의 상태가 유효하지 않습니다.");
    }

    @DisplayName("예외 테스트 : TableGroup 생성 중 다른 그룹에 속해 있는 테이블 목록을 보내면, 예외가 발생한다.")
    @Test
    void createWithNotFreeTablesExceptionTest() {
        TableGroup tableGroup = new TableGroup(테이블_그룹_ID_1, 테이블_그룹_생성시간, Collections.emptyList());
        List<OrderTable> invalidOrderTables = Arrays.asList(
                new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있음, tableGroup),
                new OrderTable(테이블_ID_2, 테이블_사람_2명, 테이블_비어있음, tableGroup)
        );
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableRequests);
        when(orderTableRepository.findAllById(anyList())).thenReturn(invalidOrderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화하려는 테이블의 상태가 유효하지 않습니다.");
    }

    @DisplayName("TableGroup에 대한 그룹 해제가 올바르게 수행된다")
    @Test
    void ungroupTest() {
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있음, null),
                new OrderTable(테이블_ID_2, 테이블_사람_2명, 테이블_비어있음, null)
        );
        TableGroup tableGroup = new TableGroup(테이블_그룹_ID_1, 테이블_그룹_생성시간, orderTables);
        when(tableGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(tableGroup));
        when(orderTableRepository.findAllById(anyList())).thenReturn(orderTables);
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(false);

        tableGroupService.ungroup(tableGroup.getId());

        verify(tableGroupRepository).delete(tableGroup);
    }

    @DisplayName("TableGroup에 대한 그룹 해제 중 주문 상태가 완료되지 않았다면 예외가 발생한다")
    @Test
    void ungroupWithStatusExceptionTest() {
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있음, null),
                new OrderTable(테이블_ID_2, 테이블_사람_2명, 테이블_비어있음, null)
        );
        TableGroup tableGroup = new TableGroup(테이블_그룹_ID_1, 테이블_그룹_생성시간, orderTables);
        when(tableGroupRepository.findById(anyLong())).thenReturn(java.util.Optional.of(tableGroup));
        when(orderTableRepository.findAllById(anyList())).thenReturn(orderTables);
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문이 완료되지 않아, 테이블을 그룹 해제할 수 없습니다.");
    }
}
