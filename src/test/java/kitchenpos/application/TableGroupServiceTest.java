package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.Fixtures;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @BeforeEach
    void setUp() {
        orderTables = new ArrayList<>();
        orderTable = Fixtures.makeOrderTable();

        orderTable2 = Fixtures.makeOrderTable();
        orderTable2.setId(2L);

        orderTables.add(orderTable);
        orderTables.add(orderTable2);

        tableGroup = Fixtures.makeTableGroup();

        tableGroup.setOrderTables(orderTables);

    }

    @DisplayName("table group 생성")
    @Test
    void create() {
        given(orderTableDao.findAllByIdIn(anyList()))
            .willReturn(orderTables);
        given(tableGroupDao.save(tableGroup))
            .willReturn(tableGroup);

        tableGroupService.create(tableGroup);

        verify(orderTableDao).findAllByIdIn(anyList());
        verify(tableGroupDao).save(tableGroup);
        verify(orderTableDao).save(orderTable);
    }

    @DisplayName("table group 해제")
    @Test
    void unGroup() {
        given(orderTableDao.findAllByTableGroupId(anyLong()))
            .willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
            .willReturn(false);

        tableGroupService.ungroup(1L);

        verify(orderTableDao).findAllByTableGroupId(anyLong());
        verify(orderDao).existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList());
        verify(orderTableDao, times(2)).save(any());
    }
}
