package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.generic.IntegrationTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.dto.GroupOrderTableRequest;
import kitchenpos.table.application.dto.TableGroupResult;
import kitchenpos.table.application.dto.TableGroupingRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends IntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void create_table_group_success() {
        // given
        final OrderTable orderTableA = generateOrderTableWithOutTableGroup(1, true);
        final OrderTable orderTableB = generateOrderTableWithOutTableGroup(3, true);
        final TableGroupingRequest request = new TableGroupingRequest(List.of(
                new GroupOrderTableRequest(orderTableA.getId()),
                new GroupOrderTableRequest(orderTableB.getId())
        ));

        // when
        final TableGroupResult savedTableGroup = tableGroupService.create(request);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @Nested
    class create_table_group_failure {

        @Test
        void order_table_is_not_exist() {
            // given
            final OrderTable orderTableA = generateOrderTable(1, false);
            final long notExistId = 1000L;
            final TableGroupingRequest request = new TableGroupingRequest(List.of(
                    new GroupOrderTableRequest(orderTableA.getId()),
                    new GroupOrderTableRequest(notExistId)
            ));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Order table does not exist.");
        }

        @Test
        void order_table_is_empty() {
            // given
            final OrderTable orderTableA = generateOrderTable(1, false);
            final TableGroupingRequest request = new TableGroupingRequest(List.of(
                    new GroupOrderTableRequest(orderTableA.getId())
            ));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Table group must have at least two tables.");
        }

        @Test
        void order_table_is_under_two() {
            // given
            final OrderTable orderTable = generateOrderTable(1, true);
            final TableGroupingRequest request = new TableGroupingRequest(List.of(
                    new GroupOrderTableRequest(orderTable.getId())
            ));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Table group must have at least two tables.");
        }

        @Test
        void any_order_table_status_is_not_empty() {
            // given
            final OrderTable orderTableA = generateOrderTable(1, true);
            final OrderTable orderTableB = generateOrderTable(3, false);
            final TableGroupingRequest request = new TableGroupingRequest(List.of(
                    new GroupOrderTableRequest(orderTableA.getId()),
                    new GroupOrderTableRequest(orderTableB.getId())
            ));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot group non-empty table or already grouped table.");
        }

        @Test
        void any_order_table_is_already_in_other_table_group() {
            // given
            final TableGroup tableGroup = generateTableGroup();
            final OrderTable orderTableA = generateOrderTable(1, true);
            final OrderTable orderTableB = generateOrderTable(3, true);
            orderTableA.groupByTableGroup(tableGroup.getId());
            orderTableB.groupByTableGroup(tableGroup.getId());
            final TableGroupingRequest request = new TableGroupingRequest(List.of(
                    new GroupOrderTableRequest(orderTableA.getId()),
                    new GroupOrderTableRequest(orderTableB.getId())
            ));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot group non-empty table or already grouped table.");
        }
    }

    @Test
    void ungroup_table_group_success() {
        // given
        final OrderTable orderTableA = generateOrderTableWithOutTableGroup(1, true);
        final OrderTable orderTableB = generateOrderTableWithOutTableGroup(2, true);
        final TableGroup tableGroup = generateTableGroup();
        orderTableA.groupByTableGroup(tableGroup.getId());
        orderTableB.groupByTableGroup(tableGroup.getId());
        generateOrder(OrderStatus.COMPLETION, orderTableA, List.of());
        generateOrder(OrderStatus.COMPLETION, orderTableB, List.of());

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        final List<OrderTable> ungroupedOrderTables = orderTableRepository.findAll();
        assertThat(ungroupedOrderTables)
                .extracting(OrderTable::getTableGroupId)
                .containsOnlyNulls();
    }

    @Nested
    class ungroup_table_group_failure {

        @Test
        void any_order_status_is_not_completion() {
            // given
            final OrderTable orderTableA = generateOrderTableWithOutTableGroup(1, false);
            final OrderTable orderTableB = generateOrderTableWithOutTableGroup(2, false);
            generateOrder(OrderStatus.COOKING, orderTableA, List.of());
            generateOrder(OrderStatus.COMPLETION, orderTableB, List.of());
            final TableGroup tableGroup = generateTableGroup();
            orderTableA.groupByTableGroup(tableGroup.getId());
            orderTableB.groupByTableGroup(tableGroup.getId());

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot ungroup non-completed table.");
        }
    }
}
