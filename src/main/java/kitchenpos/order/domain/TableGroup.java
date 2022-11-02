package kitchenpos.order.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }

    private TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = new OrderTables(orderTables);
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this(LocalDateTime.now(), orderTables);
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
