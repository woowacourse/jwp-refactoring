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

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroup(final LocalDateTime createdDate) {
        this(null, createdDate);
    }

    public void groupOrderTables(final List<OrderTable> orderTables) {
        validateSize(orderTables);
        if (isOrderTablesAbleToGroup(orderTables)) {
            this.orderTables = orderTables;
            orderTables.forEach(orderTable -> orderTable.groupByTableGroup(this));
            return;
        }
        throw new IllegalArgumentException("Cannot group non-empty table or already grouped table.");
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
        if (orderTables.stream().allMatch(OrderTable::isAbleToUnGroup)) {
            orderTables.forEach(OrderTable::ungroup);
            return;
        }
        throw new IllegalArgumentException("Cannot ungroup non-completed table.");
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
