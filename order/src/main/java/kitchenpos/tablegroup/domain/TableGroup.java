package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.annotation.Id;
import org.springframework.util.CollectionUtils;


public class TableGroup {
    @Id
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    private TableGroup() {
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup create() {
        return new TableGroup(null, LocalDateTime.now(), new ArrayList<>());
    }

    public void addOrderTables(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        boolean canAdd = orderTables.stream()
                .anyMatch(Predicate.not(OrderTable::canGroup));

        if (canAdd) {
            throw new IllegalArgumentException();
        }

        orderTables.forEach(orderTable -> orderTable.group(this.id));

        this.orderTables.addAll(orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return new ArrayList<>(orderTables);
    }
}
