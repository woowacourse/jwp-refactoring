package kitchenpos.domain.table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.exception.InvalidOrderTableSizeException;
import kitchenpos.exception.OrderTableEmptyGroupIdException;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    public TableGroup() {
    }

    private TableGroup(TableGroupBuilder tableGroupBuilder) {
        this.id = tableGroupBuilder.id;
        this.createdDate = tableGroupBuilder.createdDate;
        this.orderTables = OrderTables.create(tableGroupBuilder.orderTables);
    }

    public void updateOrderTables() {
        for (OrderTable orderTable : orderTables.getOrderTables()) {
            orderTable.changeTableGroup(this);
        }
    }

    public void unGroup() {
        orderTables.unGroup();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
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
            if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
                throw new InvalidOrderTableSizeException();
            }
            for (final OrderTable orderTable : orderTables) {
                if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                    throw new OrderTableEmptyGroupIdException();
                }
            }
            this.orderTables = orderTables;
            return this;
        }

        public TableGroup build() {
            return new TableGroup(this);
        }
    }
}
