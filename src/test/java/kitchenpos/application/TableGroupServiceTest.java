package kitchenpos.application;

import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_EMPTY_TRUE;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_NUMBER_OF_GUESTS_EMPTY;
import static kitchenpos.constants.Constants.TEST_ORDER_WRONG_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.ValueSource;

class TableGroupServiceTest extends KitchenPosServiceTest {

    @DisplayName("tableGroup 생성 - 성공")
    @Test
    void create_Success() {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()));
        orderTables.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()));
        tableGroup.setOrderTables(orderTables);

        TableGroup createdTableGroup = tableGroupService.create(tableGroup);

        assertThat(createdTableGroup.getId()).isNotNull();
        assertThat(createdTableGroup.getCreatedDate()).isNotNull();
        assertThat(createdTableGroup.getOrderTables()).hasSize(orderTables.size());

        List<Long> orderTableIds = getIds(orderTables);
        List<Long> orderTableIdsOfCreatedOrTableGroup = getIds(createdTableGroup.getOrderTables());
        assertThat(orderTableIdsOfCreatedOrTableGroup).containsAll(orderTableIds);
    }

    @DisplayName("tableGroup 생성 - 예외 발생, OrderTables가 기준 값(2)보다 작음")
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void create_OrderTablesCountLessThanDefaultCount_ThrownException(int count) {
        TableGroup tableGroup = new TableGroup();

        List<OrderTable> orderTables = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            orderTables.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()));
        }
        tableGroup.setOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("tableGroup 생성 - 예외 발생, OrderTable 일부가 존재하지 않음")
    @Test
    void create_NotExistsSomeOrderTable_ThrownException() {
        TableGroup tableGroup = new TableGroup();

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()));
        orderTables.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()));
        orderTables.add(makeOrderTableWithId(TEST_ORDER_WRONG_ID));

        tableGroup.setOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("tableGroup 생성 - 예외 발생, OrderTable 일부가 비어있지 않음")
    @Test
    void create_NotEmptySomeOrderTable_ThrownException() {
        TableGroup tableGroup = new TableGroup();

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()));
        orderTables.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()));
        orderTables.add(makeOrderTableWithId(getCreatedNotEmptyOrderTableId()));

        tableGroup.setOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("tableGroup 생성 - Table에 TableGroup 설정, 생성 성공한 경우")
    @Test
    void create_Success_SettingTableGroupAtTable() {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()));
        orderTables.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()));
        tableGroup.setOrderTables(orderTables);

        List<OrderTable> foundOrderTables = foundOrderTables(orderTables);
        for (OrderTable foundOrderTable : foundOrderTables) {
            assertThat(foundOrderTable.isEmpty()).isTrue();
            assertThat(foundOrderTable.getTableGroupId()).isNull();
        }

        TableGroup createdTableGroup = tableGroupService.create(tableGroup);
        foundOrderTables = createdTableGroup.getOrderTables();
//        foundOrderTables = foundOrderTablesByTableId(createdTableGroup.getId());

        for (OrderTable foundOrderTable : foundOrderTables) {
            assertThat(foundOrderTable.isEmpty()).isFalse();
            assertThat(foundOrderTable.getTableGroupId()).isNotNull();
        }
    }

    @DisplayName("TableGroup 해제 - 성공")
    @Test
    void ungroup_Success() {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()));
        orderTables.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()));
        tableGroup.setOrderTables(orderTables);

        TableGroup createdTableGroup = tableGroupService.create(tableGroup);
        List<OrderTable> foundOrderTables = foundOrderTablesByTableId(createdTableGroup.getId());
        for (OrderTable foundOrderTable : foundOrderTables) {
            assertThat(foundOrderTable.isEmpty()).isFalse();
            assertThat(foundOrderTable.getTableGroupId()).isNotNull();
        }

        tableGroupService.ungroup(createdTableGroup.getId());
        foundOrderTables = foundOrderTablesByTableId(createdTableGroup.getId());
        for (OrderTable foundOrderTable : foundOrderTables) {
            assertThat(foundOrderTable.isEmpty()).isFalse();
            assertThat(foundOrderTable.getTableGroupId()).isNull();
        }
    }

    @DisplayName("TableGroup 해제 - 성공, Table의 Order 상태가 Cooking/Meal이 아닌 경우")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"}, mode = Mode.EXCLUDE)
    void ungroup_OrderOfTableNotCookingOrMeal_ThrownException(OrderStatus orderStatus) {
        Long savedOrderTableId = getCreatedOrderTableWithOrderStatus(orderStatus);

        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(makeOrderTableWithId(savedOrderTableId));
        orderTables.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()));
        tableGroup.setOrderTables(orderTables);

        TableGroup createdTableGroup = tableGroupService.create(tableGroup);
        List<OrderTable> foundOrderTables = foundOrderTablesByTableId(createdTableGroup.getId());
        for (OrderTable foundOrderTable : foundOrderTables) {
            assertThat(foundOrderTable.isEmpty()).isFalse();
            assertThat(foundOrderTable.getTableGroupId()).isNotNull();
        }

        tableGroupService.ungroup(createdTableGroup.getId());
        foundOrderTables = foundOrderTablesByTableId(createdTableGroup.getId());
        for (OrderTable foundOrderTable : foundOrderTables) {
            assertThat(foundOrderTable.isEmpty()).isFalse();
            assertThat(foundOrderTable.getTableGroupId()).isNull();
        }
    }

    @DisplayName("TableGroup 해제 - 예외 발생, Table의 Order 상태가 Cooking/Meal인 경우")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void ungroup_OrderOfTableCookingOrMeal_ThrownException(OrderStatus orderStatus) {
        Long savedOrderTableId = getCreatedOrderTableWithOrderStatus(orderStatus);

        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(makeOrderTableWithId(savedOrderTableId));
        orderTables.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()));
        tableGroup.setOrderTables(orderTables);

        TableGroup createdTableGroup = tableGroupService.create(tableGroup);
        List<OrderTable> foundOrderTables = foundOrderTablesByTableId(createdTableGroup.getId());
        for (OrderTable foundOrderTable : foundOrderTables) {
            assertThat(foundOrderTable.isEmpty()).isFalse();
            assertThat(foundOrderTable.getTableGroupId()).isNotNull();
        }

        assertThatThrownBy(() -> tableGroupService.ungroup(createdTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private List<Long> getIds(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    private OrderTable makeOrderTableWithId(long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        return orderTable;
    }

    private List<OrderTable> foundOrderTablesByTableId(Long tableId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableId);

        return orderTables.stream()
            .map(this::findOrderTableById)
            .collect(Collectors.toList());
    }

    private List<OrderTable> foundOrderTables(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(this::findOrderTableById)
            .collect(Collectors.toList());
    }

    private OrderTable findOrderTableById(OrderTable orderTable) {
        Long id = orderTable.getId();
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 OrderTable이 없습니다."));
    }

    private Long getCreatedOrderTableWithOrderStatus(OrderStatus orderStatus) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS_EMPTY);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_TRUE);
        OrderTable savedOrderTable = tableService.create(orderTable);
        Long savedOrderTableId = savedOrderTable.getId();

        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        orderRepository.save(order);
        return savedOrderTableId;
    }
}
