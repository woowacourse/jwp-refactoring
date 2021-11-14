package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.Fixtures;
import kitchenpos.order.application.OrderTableService;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;
    private List<OrderTable> orderTables;
    private OrderTable orderTable;
    private OrderTable orderTable2;
    private TableGroup tableGroup;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableService orderTableService;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @BeforeEach
    void setUp() {
        orderTables = new ArrayList<>();
        orderTable = Fixtures.makeOrderTable();

        orderTable2 = new OrderTable(2L, null, 1, true);

        orderTables.add(orderTable);
        orderTables.add(orderTable2);

        tableGroup = Fixtures.makeTableGroup();
    }

    @DisplayName("table group 생성")
    @Test
    void create() {
        given(orderTableService.findAll(anyList()))
            .willReturn(orderTables);
        given(tableGroupRepository.save(any(TableGroup.class)))
            .willReturn(tableGroup);

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(1L, 2L));

        tableGroupService.create(tableGroupRequest);

        verify(orderTableService).findAll(anyList());
        verify(tableGroupRepository).save(any(TableGroup.class));
    }

    @DisplayName("table group 해제")
    @Test
    void unGroup() {
        given(orderTableService.findAllByTableGroupId(anyLong()))
            .willReturn(orderTables);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
            .willReturn(false);

        tableGroupService.ungroup(1L);

        verify(orderTableService).findAllByTableGroupId(anyLong());
        verify(orderRepository).existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList());
    }
}
