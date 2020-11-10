package kitchenpos.domain.order;

import kitchenpos.config.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.List;

@AttributeOverride(name = "id", column = @Column(name = "table_group_id"))
@Entity
public class TableGroup extends BaseEntity {
    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.EAGER)
    private List<OrderTable> orderTables;
    private LocalDateTime createdDate;

    public TableGroup() {
    }

    private TableGroup(Long id, List<OrderTable> orderTables, LocalDateTime createdDate) {
        validateOrderTablesToGrouping(orderTables);
        this.id = id;
        this.orderTables = orderTables;
        this.createdDate = createdDate;
    }

    private TableGroup(List<OrderTable> orderTables, LocalDateTime createdDate) {
        this(null, orderTables, createdDate);
    }

    public static TableGroup of(List<OrderTable> orderTables) {
        return new TableGroup(orderTables, LocalDateTime.now());
    }

    private void validateOrderTablesToGrouping(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
        for (final OrderTable orderTable : orderTables) {
            orderTable.validateGrouping();
        }
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
