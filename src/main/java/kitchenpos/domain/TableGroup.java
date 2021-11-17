package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(TableGroupBuilder tableGroupBuilder) {
        this.id = tableGroupBuilder.id;
        this.createdDate = tableGroupBuilder.createdDate;
        this.orderTables = tableGroupBuilder.orderTables;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static class TableGroupBuilder {

        private Long id;
        private LocalDateTime createdDate;
        private List<OrderTable> orderTables;

        public TableGroupBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public TableGroupBuilder setCreatedDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public TableGroupBuilder setOrderTables(List<OrderTable> orderTables) {
            this.orderTables = orderTables;
            return this;
        }

        public TableGroup build() {
            return new TableGroup(this);
        }
    }
}
