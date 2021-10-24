package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.KitchenposException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static kitchenpos.exception.KitchenposException.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    private TableGroup tableGroup;
    private OrderTable orderTable;
    private OrderTable orderTable2;
    private List<OrderTable> orderTables = new ArrayList<>();

    @BeforeEach
    void setUp() {
        tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setCreatedDate(LocalDateTime.now());

        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(true);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(3);

        orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);
        orderTable2.setNumberOfGuests(6);

        orderTables.add(orderTable);
        orderTables.add(orderTable2);
        tableGroup.setOrderTables(orderTables);
    }

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void create() {
        when(orderTableDao.findAllByIdIn(anyList()))
                .thenReturn(orderTables);
        when(tableGroupDao.save(any(TableGroup.class)))
                .thenReturn(tableGroup);

        TableGroup actual = tableGroupService.create(tableGroup);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(tableGroup);
    }

    @Test
    @DisplayName("테이블 그룹 내 테이블이 비어있거나 2개 미만이면 에러가 발생한다.")
    void createExceptionTableLessThanTwo() {
        tableGroup.setOrderTables(Collections.emptyList());
        assertThatThrownBy(()-> tableGroupService.create(tableGroup))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(ILLEGAL_TABLE_SIZE_MINIMUM);

        tableGroup.setOrderTables(Collections.singletonList(orderTable));
        assertThatThrownBy(()-> tableGroupService.create(tableGroup))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(ILLEGAL_TABLE_SIZE_MINIMUM);
    }

    @Test
    @DisplayName("테이블 그룹 내 테이블의 개수가 저장된 개수와 맞지 않으면 에러가 발생한다.")
    void createExceptionTableSize() {
        List<OrderTable> anotherOrderTables = new ArrayList<>();
        anotherOrderTables.add(orderTable);
        anotherOrderTables.add(orderTable2);
        anotherOrderTables.add(orderTable2);
        when(orderTableDao.findAllByIdIn(anyList()))
                .thenReturn(anotherOrderTables);

        assertThatThrownBy(()-> tableGroupService.create(tableGroup))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(ILLEGAL_TABLE_SIZE);
    }

    @Test
    @DisplayName("테이블 그룹 내 테이블이 비어있지 않으면 에러가 발생한다.")
    void createExceptionTableEmpty() {
        orderTable.setEmpty(false);
        when(orderTableDao.findAllByIdIn(anyList()))
                .thenReturn(orderTables);

        assertThatThrownBy(()-> tableGroupService.create(tableGroup))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(NOT_EMPTY_TABLE_TO_CREATE);
    }

    @Test
    @DisplayName("테이블 그룹 내 테이블이 그룹에 속해있으면 에러가 발생한다.")
    void createExceptionTableInGroup() {
        orderTable.setTableGroupId(1L);
        when(orderTableDao.findAllByIdIn(anyList()))
                .thenReturn(orderTables);

        assertThatThrownBy(()-> tableGroupService.create(tableGroup))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(NOT_EMPTY_TABLE_TO_CREATE);
    }

    @Test
    @DisplayName("테이블 그룹을 해제하고 테이블을 비운다.")
    void ungroup() {
        orderTable.setTableGroupId(1L);
        orderTable.setEmpty(false);
        orderTable2.setTableGroupId(1L);
        orderTable2.setEmpty(false);

        when(orderTableDao.findAllByTableGroupId(anyLong()))
                .thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .thenReturn(false);

        tableGroupService.ungroup(1L);
        assertThat(orderTable.getTableGroupId()).isNull();
        assertThat(orderTable.isEmpty()).isFalse();
        assertThat(orderTable2.getTableGroupId()).isNull();
        assertThat(orderTable2.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("식사가 완료되지 않았거나 주문이 있는 테이블은 에러가 발생한다.")
    void ungroupExceptionStatus() {
        orderTable.setTableGroupId(1L);
        orderTable.setEmpty(false);
        orderTable2.setTableGroupId(1L);
        orderTable2.setEmpty(false);

        when(orderTableDao.findAllByTableGroupId(anyLong()))
                .thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(IMPOSSIBLE_TABLE_STATUS);
    }
}
