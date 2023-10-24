package kitchenpos.domain.tablegroup;

import kitchenpos.domain.table.OrderTable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroup {
    @Id
    private final Long id;
    private final LocalDateTime createdDate;
    @MappedCollection(idColumn = "TABLE_GROUP_ID", keyColumn = "ID")
    private final List<OrderTable> orderTables;

    private TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        validate(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void validate(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : orderTables) {
            if (!savedOrderTable.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public static TableGroupBuilder builder() {
        return new TableGroupBuilder();
    }

    public TableGroup updateOrderTables(List<OrderTable> savedOrderTables) {
        return new TableGroup(id, createdDate, savedOrderTables);
    }

    public TableGroup fillTables() {
        List<OrderTable> filledTables = getOrderTables().stream()
                .map(OrderTable::fillTable)
                .collect(Collectors.toList());
        return new TableGroup(id, createdDate, filledTables);
    }

    public static final class TableGroupBuilder {
        private Long id;
        private LocalDateTime createdDate;
        private List<OrderTable> orderTables;

        private TableGroupBuilder() {
        }

        public TableGroupBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TableGroupBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public TableGroupBuilder orderTables(List<OrderTable> orderTables) {
            this.orderTables = orderTables;
            return this;
        }

        public TableGroup build() {
            return new TableGroup(id, createdDate, orderTables);
        }
    }
}
