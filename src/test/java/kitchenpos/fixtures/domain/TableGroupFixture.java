package kitchenpos.fixtures.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    public static TableGroup createTableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
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
            TableGroup tableGroup = new TableGroup();
            tableGroup.setCreatedDate(createdDate);
            tableGroup.setOrderTables(orderTables);

            return tableGroup;
        }
    }
}
