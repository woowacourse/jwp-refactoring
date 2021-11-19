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

    public TableGroup(OrderTable[] orderTables) {
        this(null, null, new OrderTables(orderTables));
    }

    public TableGroup(Long id, LocalDateTime localDateTime, List<OrderTable> orderTables) {
        this(id, localDateTime, new OrderTables(orderTables));
    }

    public TableGroup(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public void createWith(OrderTables savedOrderTables) {
        setOrderTables(savedOrderTables);
        updateCreatedDate();
    }

    public void updateCreatedDate() {
        createdDate = LocalDateTime.now();
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

    public List<Long> getOrderTableIds() {
        return orderTables.getIds();
    }

    public void setOrderTables(OrderTables orderTables) {
        this.orderTables = orderTables;
    }
}
