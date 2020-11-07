package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
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

    private List<OrderTable> tables;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        tables = Lists.newArrayList(OrderTableFixture.createEmptyWithId(OrderTableFixture.ID1),
            OrderTableFixture.createEmptyWithId(OrderTableFixture.ID2));
    }

    @DisplayName("정상적으로 테이블을 그룹화 한다.")
    @Test
    void create() {
        TableGroup tableWithoutId = TableGroupFixture.createWithoutId(tables);
        TableGroup tableWithId = TableGroupFixture.createWithId(1L, tables);
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(tables);
        when(tableGroupDao.save(tableWithoutId)).thenReturn(tableWithId);

        TableGroup savedTableGroup = tableGroupService.create(tableWithoutId);

        assertThat(savedTableGroup).isEqualToComparingFieldByField(tableWithId);
        assertThat(savedTableGroup.getOrderTables())
            .extracting(OrderTable::getTableGroupId)
            .allMatch(id -> id.equals(tableWithId.getId()));
        assertThat(savedTableGroup.getOrderTables())
            .extracting(OrderTable::isEmpty)
            .allMatch(empty -> !empty);
    }

    @DisplayName("그룹 요청 테이블의 개수가 0개인 경우 예외를 반환한다.")
    @Test
    void createWithEmptyTable() {
        TableGroup emptyTableGroup = TableGroupFixture.createWithoutId(Lists.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(emptyTableGroup))
            .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("그룹 요청 테이블의 개수가 1개인 경우 예외를 반환한다.")
    @Test
    void createWithOneTable() {
        TableGroup oneTableGroup = TableGroupFixture
            .createWithoutId(Collections.singletonList(tables.get(0)));

        assertThatThrownBy(() -> tableGroupService.create(oneTableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("실제 존재하지 않는 테이블을 그룹화하는 경우 예외를 반환한다.")
    @Test
    void createWithNotExistTable() {
        TableGroup withoutId = TableGroupFixture.createWithoutId(tables);
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Lists.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(withoutId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Empty가 아닌 상태의 테이블을 그룹화 하는 경우 예외를 반환한다.")
    @Test
    void createWithNotEmptyTable() {
        TableGroup containsEmptyTable = TableGroupFixture.createWithoutId(tables);
        tables.add(OrderTableFixture.createNotEmptyWithId(OrderTableFixture.ID3));

        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(tables);

        assertThatThrownBy(() -> tableGroupService.create(containsEmptyTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 다른 그룹이 있는 테이블을 그룹화 하는 경우 예외를 반환한다.")
    @Test
    void createWithNotNullGroupTable() {
        TableGroup containsAlreadyGroupTable = TableGroupFixture.createWithoutId(tables);
        tables.add(OrderTableFixture.createGroupTableWithId(3L));

        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(tables);

        assertThatThrownBy(() -> tableGroupService.create(containsAlreadyGroupTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 테이블을 언그룹화 한다.")
    @Test
    void ungroup() {
        TableGroup withId = TableGroupFixture.createWithId(1L, tables);
        when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(tables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
            .thenReturn(false);

        tableGroupService.ungroup(withId.getId());
        assertThat(tables)
            .extracting(OrderTable::getTableGroupId)
            .allMatch(Objects::isNull);
    }

    @DisplayName("식사를 마치치 않은 상태(Meal, Cooking)인 테이블을 Group 해제할 때 예외를 반환한다.")
    @Test
    void ungroupWithNotCompleteTable() {
        TableGroup withNotCompleteTable = TableGroupFixture.createWithId(1L, tables);
        when(orderTableDao.findAllByTableGroupId(1L))
            .thenReturn(Arrays.asList(tables.get(0), tables.get(1)));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(withNotCompleteTable.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
