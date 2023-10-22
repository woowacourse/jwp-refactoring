package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private final LocalDateTime createdDate = LocalDateTime.now();

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(final Long id) {
        this.id = id;
    }

    public TableGroup(final Long id, final List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
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

    public void addOrderTable(final OrderTable orderTable) {
        orderTables.add(orderTable);
    }
}
