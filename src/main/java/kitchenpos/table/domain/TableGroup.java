package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables;

    public TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    public void validate(final Consumer tableValidator) {
        List<OrderTable> orderTables = this.orderTables.getOrderTables();
        for (OrderTable orderTable : orderTables) {
            orderTable.validate(tableValidator);
        }
    }

    public void grouping(final List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables);
        this.orderTables.grouping();
    }

    public void ungrouping() {
        orderTables.ugrouping();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }
}
