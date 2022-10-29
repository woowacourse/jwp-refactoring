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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }

    public TableGroup(final LocalDateTime createdDate, final OrderTables orderTables) {
        this(null, createdDate, orderTables);
    }


    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this(id, createdDate, new OrderTables(orderTables));
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(final LocalDateTime createdDate) {
        this.id = null;
        this.createdDate = createdDate;
        this.orderTables = null;
    }

    public void changeOrderTablesOfGroups(final List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables);
        this.orderTables.changeGroup(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }
}
