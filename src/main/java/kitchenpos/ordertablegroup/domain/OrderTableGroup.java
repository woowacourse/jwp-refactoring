package kitchenpos.ordertablegroup.domain;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTables;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderTableGroup {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables;

    protected OrderTableGroup() {
    }

    public OrderTableGroup(final Long id, final LocalDateTime createdDate, final OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static OrderTableGroup of(final LocalDateTime now, final List<OrderTable> orderTables) {
        return new OrderTableGroup(null, now, new OrderTables(orderTables));
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
}
