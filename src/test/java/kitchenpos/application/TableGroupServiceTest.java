package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

class TableGroupServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create()")
    class create {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 새로운 테이블 그룹을 생성한다.")
        void create() {
            // given
            List<OrderTable> tables = new ArrayList<OrderTable>() {{
                add(createAndSaveOrderTable(true, null));
                add(createAndSaveOrderTable(true, null));
            }};
            TableGroup tableGroup = createTableGroup(tables);

            // when
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            // then
            assertThat(savedTableGroup.getId()).isNotNull();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("테이블 정보가 비어있거나 null인 경우 예외가 발생한다.")
        void nullAndEmptyOrderTables(List<OrderTable> tables) {
            // given
            TableGroup tableGroup = createTableGroup(tables);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블 정보가 2개 미만인 경우 예외가 발생한다.")
        void oneTables() {
            // given
            List<OrderTable> tables = new ArrayList<OrderTable>() {{
                add(createAndSaveOrderTable(true, null));
            }};
            TableGroup tableGroup = createTableGroup(tables);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("존재하지 않는 테이블인 경우 예외가 발생한다.")
        void invalidOrderTable() {
            // given
            List<OrderTable> tables = new ArrayList<OrderTable>() {{
                add(createOrderTable(true, null));
                add(createOrderTable(true, null));
            }};
            TableGroup tableGroup = createTableGroup(tables);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("비어있지 않은 테이블인 경우 예외가 발생한다.")
        void notEmptyOrderTable() {
            // given
            List<OrderTable> tables = new ArrayList<OrderTable>() {{
                add(createAndSaveOrderTable(false, null));
                add(createAndSaveOrderTable(false, null));
            }};
            TableGroup tableGroup = createTableGroup(tables);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블의 tableGroupId가 존재하는 경우 예외가 발생한다.")
        void existTableGroupId() {
            // given
            long alreadyExistTableGroupId = createAndSaveTableGroup().getId();
            List<OrderTable> tables = new ArrayList<OrderTable>() {{
                add(createAndSaveOrderTable(false, alreadyExistTableGroupId));
                add(createAndSaveOrderTable(false, alreadyExistTableGroupId));
            }};
            TableGroup tableGroup = createTableGroup(tables);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Nested
    @DisplayName("ungroup()")
    class ungroup {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 그룹이 해제된다.")
        void ungroup() {
            // given
            List<OrderTable> tables = new ArrayList<OrderTable>() {{
                add(createAndSaveOrderTable(true, null));
                add(createAndSaveOrderTable(true, null));
            }};
            TableGroup savedTableGroup = tableGroupService.create(createTableGroup(tables));

            // when
            tableGroupService.ungroup(savedTableGroup.getId());

            // then
            assertThat(orderTableDao.findAllByTableGroupId(savedTableGroup.getId())).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        @DisplayName("COOKING 혹은 MEAL 상태의 테이블인 경우 예외가 발생한다.")
        void wrongTableState(String status) {
            // given
            OrderTable orderTable1 = createAndSaveOrderTable(true, null);
            OrderTable orderTable2 = createAndSaveOrderTable(true, null);
            createAndSaveOrder(orderTable1.getId(), status);
            createAndSaveOrder(orderTable2.getId(), status);

            List<OrderTable> tables = new ArrayList<OrderTable>() {{
                add(orderTable1);
                add(orderTable2);
            }};
            TableGroup savedTableGroup = tableGroupService.create(createTableGroup(tables));

            // when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private TableGroup createTableGroup(List<OrderTable> tables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(tables);

        return tableGroup;
    }

    private TableGroup createAndSaveTableGroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroupDao.save(tableGroup);
    }

    private OrderTable createOrderTable(boolean empty, Long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(10);
        orderTable.setEmpty(empty);

        return orderTable;
    }

    private OrderTable createAndSaveOrderTable(boolean empty, Long tableGroupId) {
        OrderTable orderTable = createOrderTable(empty, tableGroupId);
        return orderTableDao.save(orderTable);
    }

    private Order createAndSaveOrder(long orderTableId, String status) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(status);

        return orderDao.save(order);
    }
}
