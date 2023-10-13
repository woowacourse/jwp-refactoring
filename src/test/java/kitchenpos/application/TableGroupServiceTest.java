package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    void testCreateSuccess() {
        //given
        final OrderTable orderTable1 = new OrderTable(1L, null, 1, true);
        final OrderTable orderTable2 = new OrderTable(2L, null, 1, true);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));
        final TableGroup expected = new TableGroup(1L, LocalDateTime.now(), List.of(orderTable1, orderTable2));

        when(orderTableDao.findAllByIdIn(any()))
                .thenReturn(List.of(orderTable1, orderTable2));
        when(tableGroupDao.save(tableGroup))
                .thenReturn(expected);


        //when
        final TableGroup result = tableGroupService.create(tableGroup);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testCreateWhenOrderTablesEmptyFailure() {
        //given
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of());

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testCreateWhenOrderTablesSizeLowerThanTwoFailure() {
        //given
        final OrderTable orderTable1 = new OrderTable(1L, null, 1, true);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1));

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testCreateWhenSavedOrderTablesAndOrderTableSizeNotMatchFailure() {
        //given
        final OrderTable orderTable1 = new OrderTable(1L, null, 1, true);
        final OrderTable orderTable2 = new OrderTable(2L, null, 1, true);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        when(orderTableDao.findAllByIdIn(any()))
                .thenReturn(List.of(orderTable1));

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testCreateWhenSavedOrderTableIsNotEmpty() {
        //given
        final OrderTable orderTable1 = new OrderTable(1L, null, 1, true);
        final OrderTable orderTable2 = new OrderTable(2L, null, 1, false);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        when(orderTableDao.findAllByIdIn(any()))
                .thenReturn(List.of(orderTable1, orderTable2));

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testCreateWhenSavedOrderTableHasTableGroupId() {
        //given
        final OrderTable orderTable1 = new OrderTable(1L, 1L, 1, true);
        final OrderTable orderTable2 = new OrderTable(2L, 1L, 1, true);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        when(orderTableDao.findAllByIdIn(any()))
                .thenReturn(List.of(orderTable1, orderTable2));

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testUngroup() {
        // given
        Long tableGroupId = 1L;
        OrderTable orderTable = new OrderTable(1L, 1L, 1, false);

        List<OrderTable> mockOrderTables = List.of(orderTable);

        when(orderTableDao.findAllByTableGroupId(any(Long.class))).thenReturn(mockOrderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(false);

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        verify(orderTableDao).findAllByTableGroupId(tableGroupId);
        verify(orderDao).existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList());
        verify(orderTableDao).save(any(OrderTable.class));

    }

    @Test
    void testUngroupWhenOrderTableAlreadyCookOrMealFailure() {
        //given
        Long tableGroupId = 1L;
        OrderTable orderTable = new OrderTable(1L, 1L, 1, false);

        List<OrderTable> mockOrderTables = List.of(orderTable);

        when(orderTableDao.findAllByTableGroupId(tableGroupId))
                .thenReturn(mockOrderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .thenReturn(true);

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);

    }
}
