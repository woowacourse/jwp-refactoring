package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup() {

    }

    public TableGroup(OrderTable[] orderTables) {
        this(null, null, Arrays.asList(orderTables));
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this(id, createdDate, null);
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
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

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
