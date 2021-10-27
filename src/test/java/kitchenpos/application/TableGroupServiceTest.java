package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("TableGroupService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블을 묶어 그룹을 지정할 수 있다 - 그룹이 지정된 테이블들은 비어 있지 않은 상태가 된다.")
    void create() {
        // given
        OrderTable table1Id = new OrderTable(1L, null, 0, false);
        OrderTable table2Id = new OrderTable(2L, null, 0, false);
        TableGroup group = new TableGroup(Arrays.asList(table1Id, table2Id));

        OrderTable table1 = new OrderTable(1L, null, 3, true);
        OrderTable table2 = new OrderTable(2L, null, 5, true);
        TableGroup savedGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(table1, table2));

        OrderTable expected_table1 = new OrderTable(1L, savedGroup.getId(), 3, false);
        OrderTable expected_table2 = new OrderTable(2L, savedGroup.getId(), 5, false);
        TableGroup expected = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(expected_table1, expected_table2));

        given(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Arrays.asList(table1, table2));
        given(tableGroupDao.save(group)).willReturn(savedGroup);

        // when
        TableGroup actual = tableGroupService.create(group);

        // then
        assertEquals(2, actual.getOrderTables().size());
        assertEquals(expected.getId(), actual.getOrderTables().get(0).getTableGroupId());
        assertEquals(expected.getId(), actual.getOrderTables().get(1).getTableGroupId());
        assertFalse(actual.getOrderTables().get(0).isEmpty());
        assertFalse(actual.getOrderTables().get(1).isEmpty());
        assertThat(actual).usingRecursiveComparison().ignoringFields("createdDate").isEqualTo(expected);
    }

    @Test
    @DisplayName("목록에 둘 이상의 테이블이 포함되어야한다.")
    void createWrongTableInsufficientTable() {
        // given
        OrderTable table1Id = new OrderTable(1L, null, 0, false);
        TableGroup group = new TableGroup(Collections.singletonList(table1Id));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(group));
        assertEquals("그룹을 지정하려면 둘 이상의 테이블이 필요합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("목록에 포함된 테이블들은 모두 등록된 테이블이여야 한다.")
    void createWrongTableNotRegister() {
        // given
        OrderTable table1Id = new OrderTable(1L, null, 0, false);
        OrderTable table2Id = new OrderTable(2L, null, 0, false);
        TableGroup group = new TableGroup(Arrays.asList(table1Id, table2Id));

        OrderTable table1 = new OrderTable(1L, null, 3, true);

        given(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Collections.singletonList(table1));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(group));
        assertEquals("등록되지 않은 테이블은 그룹으로 지정할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("목록에 포함된 테이블들은 모두 비어있어야 한다.")
    void createWrongTableNotEmpty() {
        // given
        OrderTable table1Id = new OrderTable(1L, null, 0, false);
        OrderTable table2Id = new OrderTable(2L, null, 0, false);
        TableGroup group = new TableGroup(Arrays.asList(table1Id, table2Id));

        OrderTable table1 = new OrderTable(1L, null, 3, false);
        OrderTable table2 = new OrderTable(2L, null, 5, true);

        given(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Arrays.asList(table1, table2));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(group));
        assertEquals("테이블이 비어있지 않거나 이미 다른 그룹에 속한 테이블은 그룹으로 지정할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("목록에 포함된 테이블들은 모두 소속된 다른 그룹이 없어야한다.")
    void createWrongTableInGroup() {
        // given
        OrderTable table1Id = new OrderTable(1L, null, 0, false);
        OrderTable table2Id = new OrderTable(2L, null, 0, false);
        TableGroup group = new TableGroup(Arrays.asList(table1Id, table2Id));

        OrderTable table1 = new OrderTable(1L, null, 3, true);
        OrderTable table2 = new OrderTable(2L, 1L, 5, true);

        given(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Arrays.asList(table1, table2));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(group));
        assertEquals("테이블이 비어있지 않거나 이미 다른 그룹에 속한 테이블은 그룹으로 지정할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("묶여있는 테이블 그룹을 해제할 수 있다. - 그룹이 해제된 테이블들은 비어 있지 않은 상태가 된다.")
    void ungroup() {
        // given
        Long groupId = 1L;
        OrderTable table1 = new OrderTable(1L, groupId, 3, true);
        OrderTable table2 = new OrderTable(2L, groupId, 5, true);
        given(orderTableRepository.findAllByTableGroupId(groupId)).willReturn(Arrays.asList(table1, table2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(table1.getId(), table2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        // when
        tableGroupService.ungroup(groupId);

        // then
        assertNull(table1.getTableGroupId());
        assertNull(table2.getTableGroupId());
        assertFalse(table1.isEmpty());
        assertFalse(table2.isEmpty());
    }

    @Test
    @DisplayName("목록에 포함된 테이블들의 상태가 하나라도 조리중(COOKING)이나 식사중(MEAL)인 경우 그룹을 해제할 수 없다.")
    void ungroupWrongTableCookingOrMeal() {
        // given
        Long groupId = 1L;
        OrderTable table1 = new OrderTable(1L, groupId, 3, true);
        OrderTable table2 = new OrderTable(2L, groupId, 5, true);
        given(orderTableRepository.findAllByTableGroupId(groupId)).willReturn(Arrays.asList(table1, table2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(table1.getId(), table2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.ungroup(groupId));
        assertEquals("조리중이나 식사중인 테이블을 포함하여 그룹으로 지정할 수 없습니다.", exception.getMessage());
    }
}
