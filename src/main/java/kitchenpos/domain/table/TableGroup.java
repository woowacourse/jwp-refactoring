package kitchenpos.domain.table;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    private static final int MINIMUM_SIZE = 2;

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Transient
    private List<Long> orderTableIds;

    TableGroup(Long id, LocalDateTime createdDate, List<Long> orderTableIds) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableIds = orderTableIds;
    }

    TableGroup(LocalDateTime createdDate, List<Long> orderTableIds) {
        this(null, createdDate, orderTableIds);
    }

    protected TableGroup() {
    }

    public static TableGroup from(List<Long> orderTableIds) {
        return new TableGroup(LocalDateTime.now(), orderTableIds);
    }

    public void group(List<OrderTable> orderTables) {
        validateExistenceAndDuplication(orderTableIds, orderTables);
        validateSize(orderTables);
        validateOrderTableStatus(orderTables);
        orderTables.forEach(orderTable -> orderTable.group(id));
    }

    private void validateExistenceAndDuplication(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블이 있거나 주문 테이블이 중복되었습니다.");
        }
    }

    private void validateSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_SIZE) {
            throw new IllegalArgumentException("단체 지정의 주문 테이블 개수는 최소 " + MINIMUM_SIZE + " 이상이어야 합니다.");
        }
    }

    private void validateOrderTableStatus(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (orderTable.isFilled() || orderTable.isGrouped()) {
                throw new IllegalArgumentException("단체 지정의 주문 테이블이 차 있거나 이미 단체 지정되었습니다.");
            }
        }
    }

    public void ungroup(List<OrderTable> orderTables) {
        validateGroupCanBeUngrouped(orderTables);
        orderTables.forEach(OrderTable::ungroup);
    }

    private void validateGroupCanBeUngrouped(List<OrderTable> orderTables) {
        if (hasCookingOrMealOrder(orderTables)) {
            throw new IllegalArgumentException("조리 혹은 식사 주문이 존재하는 단체 지정은 단체 지정을 취소할 수 없습니다.");
        }
    }

    private boolean hasCookingOrMealOrder(List<OrderTable> orderTables) {
        return orderTables.stream().anyMatch(OrderTable::hasCookingOrMealOrder);
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
