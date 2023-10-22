package kitchenpos.domain;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private LocalDateTime createdDate = LocalDateTime.now();

    @OneToMany(mappedBy = "tableGroup", cascade = PERSIST)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            addOrderTable(orderTable);
        }
    }

    public void addOrderTable(final OrderTable orderTable) {
        this.orderTables.add(orderTable);
        if (orderTable.getTableGroup() != this) {
            orderTable.setTableGroup(this);
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
