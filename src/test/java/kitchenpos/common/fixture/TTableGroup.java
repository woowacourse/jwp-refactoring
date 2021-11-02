package kitchenpos.common.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TTableGroup {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private LocalDateTime createdDate;
        private List<OrderTable> orderTables;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder orderTables(List<OrderTable> orderTables) {
            this.orderTables = orderTables;
            return this;
        }

        public TableGroup build() {
            TableGroup tableGroup = new TableGroup();
            tableGroup.setId(id);
            tableGroup.setCreatedDate(createdDate);
            tableGroup.setOrderTables(orderTables);
            return tableGroup;
        }
    }
}
