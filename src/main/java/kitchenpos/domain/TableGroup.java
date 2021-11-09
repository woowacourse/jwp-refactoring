package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime createdDate;
    @Transient
    private OrderTables orderTables;

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        this(null, null, new OrderTables(orderTables));
    }

    private TableGroup(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public void register() {
        if (!orderTables.canAssignTableGroup()) {
            throw new IllegalArgumentException();
        }
        orderTables.setTableGroup(this);
        createdDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public OrderTables getGroupTables() {
        return orderTables;
    }
}
