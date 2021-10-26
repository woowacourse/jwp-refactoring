package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    private TableGroup(Builder builder) {
        this.id = builder.id;
        this.createdDate = builder.createdDate;
        this.orderTables = new ArrayList<>(builder.orderTables);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private LocalDateTime createdDate;
        private List<OrderTable> orderTables;

        private Builder() {
        }

        public Builder of(TableGroup tableGroup){
            this.id = tableGroup.id;
            this.createdDate = tableGroup.createdDate;
            this.orderTables = new ArrayList<>(tableGroup.orderTables);
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder orderTables(List<OrderTable> orderTables) {
            this.orderTables = new ArrayList<>(orderTables);
            return this;
        }

        public TableGroup build() {
            return new TableGroup(this);
        }
    }

    public Long getId() {
        return id;
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
}
