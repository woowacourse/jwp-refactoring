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
import kitchenpos.domain.Table;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import kitchenpos.ui.dto.TableResponse;
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
        List<Long> tableIds = new ArrayList<>();
        tableIds.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()).getId());
        tableIds.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()).getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(tableIds);

        TableGroupResponse createdTableGroup = tableGroupService.create(tableGroupRequest);

        assertThat(createdTableGroup.getId()).isNotNull();
        assertThat(createdTableGroup.getCreatedDate()).isNotNull();
        assertThat(createdTableGroup.getOrderTables()).hasSize(tableIds.size());

        List<Long> orderTableIdsOfCreatedOrTableGroup = getIds(createdTableGroup.getOrderTables());
        assertThat(orderTableIdsOfCreatedOrTableGroup).isEqualTo(tableIds);
    }

    @DisplayName("tableGroup 생성 - 예외 발생, OrderTables가 기준 값(2)보다 작음")
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void create_OrderTablesCountLessThanDefaultCount_ThrownException(int count) {
        List<Long> tableIds = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tableIds.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()).getId());
        }
        TableGroupRequest tableGroupRequest = new TableGroupRequest(tableIds);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("tableGroup 생성 - 예외 발생, OrderTable 일부가 존재하지 않음")
    @Test
    void create_NotExistsSomeOrderTable_ThrownException() {
        List<Long> tableIds = new ArrayList<>();
        tableIds.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()).getId());
        tableIds.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()).getId());
        tableIds.add(makeOrderTableWithId(TEST_ORDER_WRONG_ID).getId());

        TableGroupRequest tableGroupRequest = new TableGroupRequest(tableIds);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("tableGroup 생성 - 예외 발생, OrderTable 일부가 비어있지 않음")
    @Test
    void create_NotEmptySomeOrderTable_ThrownException() {
        List<Long> tableIds = new ArrayList<>();
        tableIds.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()).getId());
        tableIds.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()).getId());
        tableIds.add(makeOrderTableWithId(getCreatedNotEmptyOrderTableId()).getId());

        TableGroupRequest tableGroupRequest = new TableGroupRequest(tableIds);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("tableGroup 생성 - Table에 TableGroup 설정, 생성 성공한 경우")
    @Test
    void create_Success_SettingTableGroupAtTable() {
        List<Long> tableIds = new ArrayList<>();
        tableIds.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()).getId());
        tableIds.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()).getId());

        TableGroupRequest tableGroupRequest = new TableGroupRequest(tableIds);
        for (Table foundTable : tableRepository.findAllByIdIn(tableIds)) {
            assertThat(foundTable.isEmpty()).isTrue();
            assertThat(foundTable.getTableGroup()).isNull();
        }

        tableGroupService.create(tableGroupRequest);

        for (Table foundTable : tableRepository.findAllByIdIn(tableIds)) {
            assertThat(foundTable.isEmpty()).isFalse();
            assertThat(foundTable.getTableGroup()).isNotNull();
        }
    }

    @DisplayName("TableGroup 해제 - 성공")
    @Test
    void ungroup_Success() {
        List<Long> tableIds = new ArrayList<>();
        tableIds.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()).getId());
        tableIds.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()).getId());

        TableGroupRequest tableGroupRequest = new TableGroupRequest(tableIds);

        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);
        for (Table foundTable : tableRepository.findAllByIdIn(tableIds)) {
            assertThat(foundTable.isEmpty()).isFalse();
            assertThat(foundTable.getTableGroup()).isNotNull();
        }

        tableGroupService.ungroup(tableGroupResponse.getId());
        for (Table foundTable : tableRepository.findAllByIdIn(tableIds)) {
            assertThat(foundTable.isEmpty()).isFalse();
            assertThat(foundTable.getTableGroup()).isNull();
        }
    }

    @DisplayName("TableGroup 해제 - 성공, Table의 Order 상태가 Cooking/Meal이 아닌 경우")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"}, mode = Mode.EXCLUDE)
    void ungroup_OrderOfTableNotCookingOrMeal_ThrownException(OrderStatus orderStatus) {
        Long savedOrderTableId = getCreatedOrderTableWithOrderStatus(orderStatus);

        List<Long> tableIds = new ArrayList<>();
        tableIds.add(makeOrderTableWithId(savedOrderTableId).getId());
        tableIds.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()).getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(tableIds);

        TableGroupResponse createdTableGroup = tableGroupService.create(tableGroupRequest);
        for (Table foundTable : tableRepository.findAllByIdIn(tableIds)) {
            assertThat(foundTable.isEmpty()).isFalse();
            assertThat(foundTable.getTableGroup()).isNotNull();
        }

        tableGroupService.ungroup(createdTableGroup.getId());
        for (Table foundTable : tableRepository.findAllByIdIn(tableIds)) {
            assertThat(foundTable.isEmpty()).isFalse();
            assertThat(foundTable.getTableGroup()).isNull();
        }
    }

    @DisplayName("TableGroup 해제 - 예외 발생, Table의 Order 상태가 Cooking/Meal인 경우")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void ungroup_OrderOfTableCookingOrMeal_ThrownException(OrderStatus orderStatus) {
        Long savedOrderTableId = getCreatedOrderTableWithOrderStatus(orderStatus);

        List<Long> tableIds = new ArrayList<>();
        tableIds.add(makeOrderTableWithId(savedOrderTableId).getId());
        tableIds.add(makeOrderTableWithId(getCreatedEmptyOrderTableId()).getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(tableIds);

        TableGroupResponse createdTableGroup = tableGroupService.create(tableGroupRequest);
        for (Table foundTable : tableRepository.findAllByIdIn(tableIds)) {
            assertThat(foundTable.isEmpty()).isFalse();
            assertThat(foundTable.getTableGroup()).isNotNull();
        }

        assertThatThrownBy(() -> tableGroupService.ungroup(createdTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private List<Long> getIds(List<TableResponse> tables) {
        return tables.stream()
            .map(TableResponse::getId)
            .collect(Collectors.toList());
    }

    private Table makeOrderTableWithId(long id) {
        Table table = new Table();
        table.setId(id);
        return table;
    }

    private Long getCreatedOrderTableWithOrderStatus(OrderStatus orderStatus) {
        Table table = Table
            .entityOf(TEST_ORDER_TABLE_NUMBER_OF_GUESTS_EMPTY, TEST_ORDER_TABLE_EMPTY_TRUE);
        Table savedTable = tableRepository.save(table);

        Order order = Order.entityOf(savedTable, orderStatus.name(), LocalDateTime.now(), null);
        orderRepository.save(order);
        return savedTable.getId();
    }
}
