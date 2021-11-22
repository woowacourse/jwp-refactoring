package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.testutils.TestDomainBuilder.orderTableBuilder;
import static kitchenpos.testutils.TestDomainBuilder.tableGroupBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@MockitoSettings
class TableGroupServiceTest {

    private static final Long NON_EXISTENT_ID = 987654321L;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("테이블을 그룹화한다")
    @Test
    void create() {
        // given
        TableGroup tableGroup = tableGroupBuilder()
                .id(1L)
                .orderTables(Arrays.asList(
                        orderTableBuilder().id(1L).build(),
                        orderTableBuilder().id(2L).build()
                ))
                .build();

        OrderTable savedOrderTable1 = orderTableBuilder()
                .id(1L)
                .empty(true)
                .tableGroupId(null)
                .build();
        OrderTable savedOrderTable2 = orderTableBuilder()
                .id(2L)
                .empty(true)
                .tableGroupId(null)
                .build();
        List<OrderTable> savedOrderTables = Arrays.asList(savedOrderTable1, savedOrderTable2);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(savedOrderTables);
        given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup.getCreatedDate()).isNotNull();
        assertThat(savedOrderTables).extracting(OrderTable::getTableGroupId).doesNotContainNull();
        assertThat(savedOrderTables).extracting(OrderTable::isEmpty).containsExactly(false, false);
        assertThat(savedTableGroup.getOrderTables()).isNotNull();

        then(orderTableDao).should(times(1)).findAllByIdIn(anyList());
        then(tableGroupDao).should(times(1)).save(tableGroup);
        then(orderTableDao).should(times(2)).save(any(OrderTable.class));
    }

    @DisplayName("테이블 그룹화에 실패한다 - 테이블이 2개 미만인 경우")
    @Test
    void createWhenLessTable() {
        // given
        TableGroup tableGroup = tableGroupBuilder()
                .id(1L)
                .orderTables(Collections.singletonList(
                        orderTableBuilder().id(1L).build()
                ))
                .build();

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);

        then(tableGroupDao).should(never()).save(any());
        then(orderTableDao).should(never()).save(any());
    }

    @DisplayName("테이블 그룹화에 실패한다 - 존재하지 않는 테이블이 있는 경우")
    @Test
    void createWhenHasNonExistentTable() {
        // given
        TableGroup tableGroup = tableGroupBuilder()
                .id(1L)
                .orderTables(Arrays.asList(
                        orderTableBuilder().id(1L).build(),
                        orderTableBuilder().id(NON_EXISTENT_ID).build()
                ))
                .build();

        OrderTable savedOrderTable = orderTableBuilder()
                .id(1L)
                .empty(true)
                .tableGroupId(null)
                .build();
        List<OrderTable> savedOrderTables = Collections.singletonList(savedOrderTable);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(savedOrderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);

        then(tableGroupDao).should(never()).save(any());
        then(orderTableDao).should(never()).save(any());
    }

    @DisplayName("테이블 그룹화에 실패한다 - 중복 테이블이 있는 경우")
    @Test
    void createWhenHasDuplicatedTable() {
        // given
        TableGroup tableGroup = tableGroupBuilder()
                .id(1L)
                .orderTables(Arrays.asList(
                        orderTableBuilder().id(1L).build(),
                        orderTableBuilder().id(1L).build()
                ))
                .build();

        OrderTable savedOrderTable = orderTableBuilder()
                .id(1L)
                .empty(true)
                .tableGroupId(null)
                .build();
        List<OrderTable> savedOrderTables = Collections.singletonList(savedOrderTable);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(savedOrderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);

        then(tableGroupDao).should(never()).save(any());
        then(orderTableDao).should(never()).save(any());
    }

    @DisplayName("테이블 그룹화에 실패한다 - 비어있지 않은 테이블이 존재하는 경우")
    @Test
    void createWhenHasNotEmptyTable() {
        // given
        TableGroup tableGroup = tableGroupBuilder()
                .id(1L)
                .orderTables(Arrays.asList(
                        orderTableBuilder().id(1L).build(),
                        orderTableBuilder().id(2L).build()
                ))
                .build();

        OrderTable savedOrderTable1 = orderTableBuilder()
                .id(1L)
                .empty(false)
                .tableGroupId(null)
                .build();
        OrderTable savedOrderTable2 = orderTableBuilder()
                .id(2L)
                .empty(true)
                .tableGroupId(null)
                .build();
        List<OrderTable> savedOrderTables = Arrays.asList(savedOrderTable1, savedOrderTable2);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(savedOrderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);

        then(tableGroupDao).should(never()).save(any());
        then(orderTableDao).should(never()).save(any());
    }

    @DisplayName("테이블 그룹화에 실패한다 - 이미 그룹화된 테이블이 존재하는 경우")
    @Test
    void createWhenHasGroupTable() {
        // given
        TableGroup tableGroup = tableGroupBuilder()
                .id(1L)
                .orderTables(Arrays.asList(
                        orderTableBuilder().id(1L).build(),
                        orderTableBuilder().id(2L).build()
                ))
                .build();

        OrderTable savedOrderTable1 = orderTableBuilder()
                .id(1L)
                .empty(true)
                .tableGroupId(1L)
                .build();
        OrderTable savedOrderTable2 = orderTableBuilder()
                .id(2L)
                .empty(true)
                .tableGroupId(null)
                .build();
        List<OrderTable> savedOrderTables = Arrays.asList(savedOrderTable1, savedOrderTable2);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(savedOrderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);

        then(tableGroupDao).should(never()).save(any());
        then(orderTableDao).should(never()).save(any());
    }

    @DisplayName("테이블의 그룹화를 해제한다")
    @Test
    void ungroup() {
        // given
        Long tableGroupId = 1L;

        OrderTable savedOrderTable1 = orderTableBuilder()
                .id(1L)
                .tableGroupId(tableGroupId)
                .build();
        OrderTable savedOrderTable2 = orderTableBuilder()
                .id(2L)
                .tableGroupId(tableGroupId)
                .build();
        List<OrderTable> savedOrderTables = Arrays.asList(savedOrderTable1, savedOrderTable2);

        given(orderTableDao.findAllByTableGroupId(tableGroupId)).willReturn(savedOrderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        assertThat(savedOrderTables).extracting(OrderTable::getTableGroupId).containsExactly(null, null);
        assertThat(savedOrderTables).extracting(OrderTable::isEmpty).containsExactly(false, false);

        then(orderTableDao).should(times(1)).findAllByTableGroupId(tableGroupId);
        then(orderDao).should(times(1))
                .existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList());
        then(orderTableDao).should(times(2)).save(any(OrderTable.class));
    }

    @DisplayName("테이블의 그룹화 해제에 실패한다 - 완료가 아닌 조리 또는 식사 상태의 주문이 있는 경우")
    @Test
    void ungroupWhenHasNotCompletedOrder() {
        // given
        Long tableGroupId = 1L;

        OrderTable savedOrderTable1 = orderTableBuilder()
                .id(1L)
                .tableGroupId(tableGroupId)
                .build();
        OrderTable savedOrderTable2 = orderTableBuilder()
                .id(2L)
                .tableGroupId(tableGroupId)
                .build();
        List<OrderTable> savedOrderTables = Arrays.asList(savedOrderTable1, savedOrderTable2);

        given(orderTableDao.findAllByTableGroupId(tableGroupId)).willReturn(savedOrderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderTableDao).should(never()).save(any());
    }
}
