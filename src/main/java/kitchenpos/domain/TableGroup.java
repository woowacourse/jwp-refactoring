package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    private LocalDateTime createdDate = LocalDateTime.now();

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroup of(final List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup(orderTables);

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            if (orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }

        orderTables.forEach(orderTable -> orderTable.changeTableGroup(tableGroup));
        return tableGroup;
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
        orderTables.removeAll(orderTables);
    }
}
