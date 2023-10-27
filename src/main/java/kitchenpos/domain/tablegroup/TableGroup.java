package kitchenpos.domain.tablegroup;

import kitchenpos.domain.ordertable.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @Transient
    private List<OrderTable> orderTables;

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    private static void validateOrderTables(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("[ERROR] 2개 이상의 테이블만 그룹으로 묶을 수 있습니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            final Long tableGroupId = orderTable.getTableGroupId();
            if (!orderTable.isEmpty() || Objects.nonNull(tableGroupId)) {
                throw new IllegalArgumentException("[ERROR] 비어있거나, 이미 테이블 그룹이 존재하는 테이블은 그룹에 넣을 수 없습니다.");
            }
        }
    }

    protected TableGroup() {
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

    public void changeOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
