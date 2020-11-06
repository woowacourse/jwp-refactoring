package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    OrderTable orderTable1;
    OrderTable orderTable2;
    OrderTable notEmptyTable;
    OrderTable alreadyGroupTable;

    private TableGroup tableGroup1;
    private TableGroup tableGroup2;
    private List<OrderTable> tables;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        orderTable1 = OrderTableFixture.createEmptyWithId(OrderTableFixture.ID1);
        orderTable2 = OrderTableFixture.createEmptyWithId(OrderTableFixture.ID2);
        notEmptyTable = OrderTableFixture.createNotEmptyWithId(OrderTableFixture.ID3);
        alreadyGroupTable = OrderTableFixture.createGroupTableWithId(OrderTableFixture.ID4);

        tables = Lists.newArrayList(orderTable1, orderTable2);

        tableGroup1 = TableGroupFixture.createWithoutId(tables);
        tableGroup2 = TableGroupFixture.createWithId(TableGroupFixture.ID1, tables);
    }

    @DisplayName("정상적으로 테이블을 그룹화 한다.")
    @Test
    void create() {
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(tables);
        when(tableGroupDao.save(any(TableGroup.class))).thenReturn(tableGroup2);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup1);

        assertThat(savedTableGroup)
            .isEqualToComparingFieldByField(tableGroup2);
        assertThat(savedTableGroup.getOrderTables())
            .extracting(OrderTable::getTableGroupId)
            .allMatch(id -> id.equals(tableGroup2.getId()));
        assertThat(savedTableGroup.getOrderTables())
            .extracting(OrderTable::isEmpty)
            .allMatch(empty -> !empty);
    }

    @DisplayName("그룹 요청 테이블의 개수가 2개 미만인 경우 예외를 반환한다")
    @Test
    void createEmptyTable() {
        TableGroup emptyTableGroup = new TableGroup();

        assertThatThrownBy(() -> tableGroupService.create(emptyTableGroup))
            .isInstanceOf(IllegalArgumentException.class);

        emptyTableGroup.setOrderTables(Arrays.asList(orderTable1));

        assertThatThrownBy(() -> tableGroupService.create(emptyTableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("실제 저장되어 있지 않은 테이블 아이디를 포함하여 그룹하는 경우 예외를 반환한다.")
    @Test
    void createWithNotExistTable() {
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Lists.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Empty가 아닌 상태의 테이블을 그룹화 하는 경우 예외를 반환한다.")
    @Test
    void createWithNotEmptyTable() {
        tables.add(notEmptyTable);
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(tables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 그룹이 있는 테이블을 그룹화 하는 경우 예외를 반환한다.")
    @Test
    void createWithNotNullGroupTable() {
        tables.add(alreadyGroupTable);
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(tables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 테이블을 언그룹화 한다.")
    @Test
    void ungroup() {
        when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(tables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
            .thenReturn(false);

        tableGroupService.ungroup(tableGroup2.getId());
        assertThat(tables)
            .extracting(OrderTable::getTableGroupId)
            .allMatch(Objects::isNull);
    }

    @DisplayName("식사를 마치지 않은 테이블을 언그룹할 때 예외를 반환한다.")
    @Test
    void ungroupWithNotCompleteTable() {
        when(orderTableDao.findAllByTableGroupId(1L))
            .thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
