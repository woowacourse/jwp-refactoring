package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        groupOrderTables(orderTables);
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void groupOrderTables(final List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.group(id);
        }
    }

    public void ungroup(final Supplier<Boolean> condition) {
        if (condition.get()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
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
}
