package kitchenpos.tablegroup.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private final LocalDateTime createdDate = LocalDateTime.now();

    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
    }

    public TableGroup(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public void addOrderTable(final OrderTable orderTable) {
        orderTables.addTable(orderTable);
    }
}
