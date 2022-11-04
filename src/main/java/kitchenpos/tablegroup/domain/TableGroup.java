package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.tablegroup.service.TableValidator;

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

    public void validate(final TableValidator tableValidator) {
        tableValidator.validatePossibleUngrouping(this);
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
