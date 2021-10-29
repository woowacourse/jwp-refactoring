package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @Transient
    private OrderTables orderTables;

    public TableGroup() {
    }

    private TableGroup(Builder builder) {
        this.id = builder.id;
        this.createdDate = builder.createdDate;
        this.orderTables = builder.orderTables;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private LocalDateTime createdDate;
        private OrderTables orderTables;

        private Builder() {
        }

        public Builder of(TableGroup tableGroup) {
            this.id = tableGroup.id;
            this.createdDate = tableGroup.createdDate;
            this.orderTables = tableGroup.orderTables;
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
            this.orderTables = new OrderTables(orderTables);
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
        return orderTables.getOrderTables();
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables);
    }
}
