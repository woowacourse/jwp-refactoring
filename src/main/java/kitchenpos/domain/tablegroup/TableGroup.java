package kitchenpos.domain.tablegroup;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup extends AbstractAggregateRoot<TableGroup> {
    @Id
    private Long id;
    private LocalDateTime createdDate;

    public TableGroup(LocalDateTime createdDate) {
        this(null, createdDate);
    }

    @PersistenceCreator
    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroup group(List<Long> orderTableIds) {
        validateOrderTables(orderTableIds);
        return this.andEvent(new GroupingEvent(this, orderTableIds));
    }

    public void unGroup() {
        registerEvent(new UnGroupEvent(this));
    }

    private void validateOrderTables(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException("주문 테이블은 2개 이상이여야 합니다");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
