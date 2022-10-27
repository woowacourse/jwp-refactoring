package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.TableGroupCreateRequest;

class TableGroupServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create()")
    class CreateMethod {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 새로운 테이블 그룹을 생성한다.")
        void create() {
            // given
            OrderTable orderTable1 = createAndSaveOrderTable(true, null);
            OrderTable orderTable2 = createAndSaveOrderTable(true, null);
            TableGroupCreateRequest request = new TableGroupCreateRequest(
                new ArrayList<OrderTableCreateRequest>() {{
                    add(new OrderTableCreateRequest(orderTable1.getId()));
                    add(new OrderTableCreateRequest(orderTable2.getId()));
                }}
            );

            // when
            TableGroup savedTableGroup = tableGroupService.create(request);

            // then
            assertThat(savedTableGroup.getId()).isNotNull();
        }

        @Test
        @DisplayName("존재하지 않는 테이블인 경우 예외가 발생한다.")
        void invalidOrderTable() {
            // given
            TableGroupCreateRequest request = new TableGroupCreateRequest(
                new ArrayList<OrderTableCreateRequest>() {{
                    add(new OrderTableCreateRequest(0L));
                    add(new OrderTableCreateRequest(0L));
                }}
            );

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않은 테이블입니다.");
        }

        @Test
        @DisplayName("비어있지 않은 테이블인 경우 예외가 발생한다.")
        void notEmptyOrderTable() {
            // given
            OrderTable orderTable1 = createAndSaveOrderTable(false, null);
            OrderTable orderTable2 = createAndSaveOrderTable(false, null);
            TableGroupCreateRequest request = new TableGroupCreateRequest(
                new ArrayList<OrderTableCreateRequest>() {{
                    add(new OrderTableCreateRequest(orderTable1.getId()));
                    add(new OrderTableCreateRequest(orderTable2.getId()));
                }}
            );

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있지 않은 테이블입니다.");
        }

        @Test
        @DisplayName("테이블의 tableGroupId가 존재하는 경우 예외가 발생한다.")
        void existTableGroupId() {
            // given
            long alreadyExistTableGroupId = createAndSaveTableGroup().getId();
            OrderTable orderTable1 = createAndSaveOrderTable(true, alreadyExistTableGroupId);
            OrderTable orderTable2 = createAndSaveOrderTable(true, alreadyExistTableGroupId);
            TableGroupCreateRequest request = new TableGroupCreateRequest(
                new ArrayList<OrderTableCreateRequest>() {{
                    add(new OrderTableCreateRequest(orderTable1.getId()));
                    add(new OrderTableCreateRequest(orderTable2.getId()));
                }}
            );

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 다른 그룹에 존재하는 테이블입니다.");
        }

    }

    @Nested
    @DisplayName("ungroup()")
    class UngroupMethod {

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
            Order order1 = orderDao.save(new Order(orderTable1.getId()));
            order1.changeStatus(status);

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
        tableGroup.setOrderTables(tables);

        return tableGroup;
    }

    private TableGroup createAndSaveTableGroup() {
        TableGroup tableGroup = new TableGroup();
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

}
