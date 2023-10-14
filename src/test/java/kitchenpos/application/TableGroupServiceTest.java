package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends IntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void create_table_group_success() {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(
                generateOrderTableWithOutTableGroup(1, true),
                generateOrderTableWithOutTableGroup(4, true)
        ));
        tableGroup.setCreatedDate(LocalDateTime.now());

        // when
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedTableGroup.getId()).isNotNull();
            softly.assertThat(savedTableGroup.getOrderTables())
                    .extracting(OrderTable::isEmpty)
                    .containsOnly(false);
        });
    }

    @Nested
    class create_table_group_failure {

        @Test
        void order_table_size_is_less_than_two() {
            // given
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(new OrderTable()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void order_table_size_is_empty() {
            // given
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void order_table_is_not_exist() {
            // given
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(
                    new OrderTable(),
                    new OrderTable()
            ));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void any_order_table_status_is_not_empty() {
            // given
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(
                    generateOrderTable(1, true),
                    generateOrderTable(4, false)
            ));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void any_order_table_is_already_in_other_table_group() {
            // given
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(
                    generateOrderTable(1, true, generateTableGroup()),
                    generateOrderTable(4, true, generateTableGroup())
            ));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void ungroup_table_group_success() {
        // given
        final TableGroup tableGroup = generateTableGroup();
        OrderTable orderTable = generateOrderTable(1, true, tableGroup);
        tableGroup.setOrderTables(List.of(orderTable));
        generateOrder(OrderStatus.COMPLETION, orderTable);
        generateOrder(OrderStatus.COMPLETION, orderTable);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        final List<OrderTable> ungroupedOrderTables = orderTableDao.findAll();
        assertThat(ungroupedOrderTables)
                .extracting(OrderTable::getTableGroupId)
                .containsOnlyNulls();
    }

    @Nested
    class ungroup_table_group_failure {

        @Test
        void any_order_status_is_not_completion() {
            // given
            final TableGroup tableGroup = generateTableGroup();
            OrderTable orderTable = generateOrderTable(1, true, tableGroup);
            tableGroup.setOrderTables(List.of(orderTable));
            generateOrder(OrderStatus.COOKING, orderTable);
            generateOrder(OrderStatus.COMPLETION, orderTable);

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
