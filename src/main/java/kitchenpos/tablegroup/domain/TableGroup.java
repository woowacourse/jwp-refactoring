package kitchenpos.tablegroup.domain;

import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<Long> orderTableIds;

    public TableGroup(Long id, LocalDateTime createdDate) {
        this(id, createdDate, null);
    }

    public TableGroup(LocalDateTime createdDate, List<Long> orderTableIds) {
        this(null, createdDate, orderTableIds);
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<Long> orderTableIds) {
        validate(orderTableIds);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableIds = orderTableIds;
    }

    private void validate(List<Long> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public void ungroup() {
        orderTableIds = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
