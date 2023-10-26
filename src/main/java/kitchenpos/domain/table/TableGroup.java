package kitchenpos.domain.table;

import kitchenpos.domain.order.OrderTables;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    public TableGroup(final OrderTables orderTables) {
        validateGroupable(orderTables);

        this.orderTables = orderTables;
    }

    protected TableGroup() {
    }

    private void validateGroupable(final OrderTables orderTables) {
        if (orderTables.isNotGroupable()) {
            throw new IllegalArgumentException();
        }
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
