package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGroup {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    public TableGroup() {
    }

    public TableGroup(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTablesValue() {
        return orderTables.getValue();
    }

    public void updateOrderTables(List<OrderTable> orderTables) {
        this.orderTables = OrderTables.from(orderTables);
        this.orderTables.group(this);
    }

    public void ungroup() {
        orderTables.ungroup();
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
