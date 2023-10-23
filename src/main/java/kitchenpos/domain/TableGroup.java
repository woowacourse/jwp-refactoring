package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        orderTables.forEach(it -> it.joinTableGroup(this));

        this.orderTables = orderTables;
        this.createdDate = LocalDateTime.now();
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        orderTables.forEach(this::validateOrderTable);
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException();
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
