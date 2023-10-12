package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends IntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;

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
                    generateOrderTable(1, true),
                    generateOrderTable(4, true)
            ));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
