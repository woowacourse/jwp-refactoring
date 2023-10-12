package kitchenpos.fixtures.domain;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TableGroupFixture {

    public static TableGroup createTableGroup(final LocalDateTime createdDate,
                                              final List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    public static class TableGroupRequestBuilder {

        private final List<OrderTable> orderTables = new ArrayList<>();

        private LocalDateTime createdDate = LocalDateTime.now();

        public TableGroupRequestBuilder createdDate(final LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public TableGroupRequestBuilder addOrderTables(final Collection<OrderTable> orderTables) {
            this.orderTables.addAll(orderTables);
            return this;
        }

        public TableGroupRequestBuilder addOrderTables(final OrderTable... orderTables) {
            this.orderTables.addAll(List.of(orderTables));
            return this;
        }

        public TableGroup build() {
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setCreatedDate(createdDate);
            tableGroup.setOrderTables(orderTables);
            return tableGroup;
        }
    }
}
