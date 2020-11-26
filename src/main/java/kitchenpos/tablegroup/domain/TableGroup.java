package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.table.domain.OrderTables;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    public static TableGroupBuilder builder() {
        return new TableGroupBuilder();
    }

    public List<Long> extractOrderTableIds() {
        return orderTables.extractIds();
    }

    public void modifyOrderTables(final OrderTables orderTables) {
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
    }

    public void ungroupTables() {
        orderTables.ungroup();
    }

    public static class TableGroupBuilder {
        private Long id;
        private LocalDateTime createdDate;
        private OrderTables orderTables;

        public TableGroupBuilder() {
        }

        public TableGroupBuilder(Long id, LocalDateTime createdDate, OrderTables orderTables) {
            this.id = id;
            this.createdDate = createdDate;
            this.orderTables = orderTables;
        }

        public TableGroupBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TableGroupBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public TableGroupBuilder orderTables(OrderTables orderTables) {
            this.orderTables = orderTables;
            return this;
        }

        public TableGroup build() {
            return new TableGroup(id, createdDate, orderTables);
        }
    }
}
