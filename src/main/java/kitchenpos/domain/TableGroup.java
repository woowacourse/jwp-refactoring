package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
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

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        validate(orderTables);
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void validate(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            validateTable(orderTable);
        }
    }

    private void validateTable(OrderTable orderTable) {
        if (!orderTable.isEmpty() || orderTable.existsTableGroup()) {
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
