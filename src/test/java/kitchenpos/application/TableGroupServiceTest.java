package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.request.TableGroupRequest.OrderTableOfGroupRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TableGroupServiceTest extends ServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정 저장")
    @Test
    void create() {
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 0, true),
            new OrderTable(2L, null, 0, true)
        );
        long newTableGroupId = 1L;
        when(orderTableRepository.findAllByIdIn(convertIdsFromOrderTables(orderTables))).thenReturn(
            orderTables);
        when(tableGroupRepository.save(any(TableGroup.class))).thenAnswer(invocation -> {
            TableGroup tableGroup = invocation.getArgument(0);
            return new TableGroup(newTableGroupId, tableGroup.getOrderTables());
        });

        TableGroupResponse actual = tableGroupService.create(
            new TableGroupRequest(orderTableRequestFromEntity(orderTables))
        );
        List<OrderTableResponse> expectedOrderTablesResponse = orderTables.stream()
            .map(orderTable -> newOrderTableResponse(newTableGroupId, orderTable))
            .collect(Collectors.toList());
        TableGroupResponse expected = new TableGroupResponse(1L, expectedOrderTablesResponse);

        verify(tableGroupRepository, times(1)).save(any(TableGroup.class));
        verify(orderTableRepository, times(orderTables.size())).save(
            argThat(orderTable ->
                !orderTable.isEmpty() && Objects.nonNull(orderTable.getTableGroup().getId())
            )
        );
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    private OrderTableResponse newOrderTableResponse(final long newTableGroupId,
                                                     final OrderTable orderTable) {
        return new OrderTableResponse(
            orderTable.getId(),
            newTableGroupId,
            orderTable.getNumberOfGuests().getValue(),
            orderTable.isEmpty()
        );
    }

    private List<OrderTableOfGroupRequest> orderTableRequestFromEntity(
        final List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(orderTable -> new OrderTableOfGroupRequest(orderTable.getId()))
            .collect(Collectors.toList());
    }

    @DisplayName("1개 이하의 주문 테이블로 단체 지정을 등록할 경우 예외 처리")
    @Test
    void createWhenOrderTableSizeIsLessThan2() {
        List<OrderTable> orderTables = Collections.singletonList(
            new OrderTable(1L, null, 0, true)
        );

        assertThatThrownBy(() -> tableGroupService.create(
            new TableGroupRequest(orderTableRequestFromEntity(orderTables))
        )).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 테이블로 단체 지정을 등록할 경우 예외 처리")
    @Test
    void createWhenSomeOrderTablesCannotBeFound() {
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 0, true),
            new OrderTable(2L, null, 0, true)
        );
        when(orderTableRepository.findAllByIdIn(convertIdsFromOrderTables(orderTables))).thenReturn(
            Collections.singletonList(orderTables.get(0))
        );

        assertThatThrownBy(() -> tableGroupService.create(
            new TableGroupRequest(orderTableRequestFromEntity(orderTables))
        )).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있지 않은 테이블로 단체 지정을 등록할 경우 예외 처리")
    @Test
    void createWhenSomeOrderTablesAreNotEmpty() {
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 0, true),
            new OrderTable(2L, null, 0, false)
        );
        when(orderTableRepository.findAllByIdIn(convertIdsFromOrderTables(orderTables))).thenReturn(
            orderTables
        );

        assertThatThrownBy(() -> tableGroupService.create(
            new TableGroupRequest(orderTableRequestFromEntity(orderTables))
        )).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 단체 지정이 된 테이블을 단체 지정으로 등록할 경우 예외 처리")
    @Test
    void createWhenSomeOrderTablesAreAlreadyDesignatedAsGroup() {
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 0, true),
            new OrderTable(2L, new TableGroup(1L, null), 0, true)
        );
        when(orderTableRepository.findAllByIdIn(convertIdsFromOrderTables(orderTables))).thenReturn(
            orderTables
        );

        assertThatThrownBy(() -> tableGroupService.create(
            new TableGroupRequest(orderTableRequestFromEntity(orderTables))
        )).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 제거")
    @Test
    void ungroup() {
        long tableGroupId = 1L;
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 0, false),
            new OrderTable(2L, null, 0, false)
        );
        when(orderTableRepository.findAllByTableGroupId(tableGroupId)).thenReturn(orderTables);
        when(
            orderDao.existsByOrderTableIdInAndOrderStatusIn(
                convertIdsFromOrderTables(orderTables),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
            )
        ).thenReturn(false);

        tableGroupService.ungroup(tableGroupId);

        verify(orderTableRepository, times(orderTables.size())).save(
            argThat(orderTable ->
                !orderTable.isEmpty() && Objects.isNull(orderTable.getTableGroup())
            )
        );
    }

    @DisplayName("조리나 식사 상태인 테이블이 있는 단체 지정을 제거할 경우 예외 처리")
    @Test
    void deleteWithNotFoundTableGroup() {
        long tableGroupId = 1L;
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 0, false),
            new OrderTable(2L, null, 0, false)
        );
        when(orderTableRepository.findAllByTableGroupId(tableGroupId)).thenReturn(orderTables);
        when(
            orderDao.existsByOrderTableIdInAndOrderStatusIn(
                convertIdsFromOrderTables(orderTables),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
            )
        ).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId));
    }

    private List<Long> convertIdsFromOrderTables(final List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }
}
