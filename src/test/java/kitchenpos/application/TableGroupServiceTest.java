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
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

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
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        orderTables = new ArrayList<>();
        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(1);

        orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setNumberOfGuests(1);

        orderTables.add(orderTable);
        orderTables.add(orderTable2);

        tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setCreatedDate(LocalDateTime.now());
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
