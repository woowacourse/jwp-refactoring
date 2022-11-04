package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.order.domain.OrderTable;
import org.springframework.util.CollectionUtils;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    private TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate) {
        this(null, createdDate, Collections.emptyList());
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        validateNull(createdDate);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void validateNull(LocalDateTime createdDate) {
        if (createdDate == null) {
            throw new IllegalArgumentException("[ERROR] 생성 날짜가 없으면 안됩니다.");
        }
    }

    public void addOrderTables(final List<OrderTable> orderTables) {
        validateSize(orderTables);
        this.orderTables = orderTables;
    }

    private void validateSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
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
