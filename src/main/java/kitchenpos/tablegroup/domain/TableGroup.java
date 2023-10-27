package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import kitchenpos.ordertable.domain.OrderTables;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

public class TableGroup {
    @Id
    private Long id;
    private LocalDateTime createdDate;
    @Embedded.Empty
    private OrderTables orderTables;

    public TableGroup(final Long id, final LocalDateTime createdDate, final OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(final LocalDateTime createdDate, final OrderTables orderTables) {
        this(null, createdDate, orderTables);
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
