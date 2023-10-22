package kitchenpos.domain;

import kitchenpos.domain.table.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate) {
        this(null, createdDate);
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public void changeOrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        for (OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(this.id);
        }
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
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
