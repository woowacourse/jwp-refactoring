package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    private static final int MINIMUM_TABLE_SIZE = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
        orderTables.forEach(orderTable -> orderTable.groupByTableGroup(this));
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        validateSize(orderTables);
        validateGroupOrderTableIsAvailable(orderTables);
    }

    private void validateGroupOrderTableIsAvailable(final List<OrderTable> orderTables) {
        if (!isOrderTablesAbleToGroup(orderTables)) {
            throw new IllegalArgumentException("Cannot group non-empty table or already grouped table.");
        }
    }

    private void validateSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_TABLE_SIZE) {
            throw new IllegalArgumentException("Table group must have at least two tables.");
        }
    }

    private boolean isOrderTablesAbleToGroup(final List<OrderTable> orderTables) {
        return orderTables.stream().allMatch(OrderTable::isAbleToGroup);
    }

    public void ungroupOrderTables() {
        if (isOrderTablesAbleToUngroup()) {
            orderTables.forEach(OrderTable::ungroup);
            return;
        }
        throw new IllegalArgumentException("Cannot ungroup non-completed table.");
    }

    private boolean isOrderTablesAbleToUngroup() {
        return orderTables.stream().allMatch(OrderTable::isAbleToUnGroup);
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
}
