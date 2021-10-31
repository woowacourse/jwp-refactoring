package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.Fixtures;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
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
    private OrderTableRepository orderTableRepository;

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

        tableGroup.addOrderTables(orderTables);

    }

    @DisplayName("table group 생성")
    @Test
    void create() {
        given(orderTableRepository.findAllByIdIn(anyList()))
            .willReturn(orderTables);
        given(tableGroupRepository.save(any(TableGroup.class)))
            .willReturn(tableGroup);

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(1L, 2L));

        tableGroupService.create(tableGroupRequest);

        verify(orderTableRepository).findAllByIdIn(anyList());
        verify(tableGroupRepository).save(any(TableGroup.class));
        verify(orderTableRepository, times(2)).save(any(OrderTable.class));
    }

    @DisplayName("table group 해제")
    @Test
    void unGroup() {
        given(orderTableRepository.findAllByTableGroupId(anyLong()))
            .willReturn(orderTables);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
            .willReturn(false);

        tableGroupService.ungroup(1L);

        verify(orderTableRepository).findAllByTableGroupId(anyLong());
        verify(orderRepository).existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList());
    }
}
