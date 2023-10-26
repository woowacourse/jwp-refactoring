package kitchenpos.tablegroup.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.tablegroup.vo.GroupTableIds;
import kitchenpos.tablegroup.vo.GroupTables;
import kitchenpos.tablegroup.vo.TableOrders;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    private static final int MINIMUM_SIZE = 2;

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    TableGroup(LocalDateTime createdDate) {
        this(null, createdDate);
    }

    protected TableGroup() {
    }

    public static TableGroup from(LocalDateTime createdDate) {
        return new TableGroup(createdDate);
    }

    public void group(List<OrderTable> orderTables, GroupTableIds groupTableIds) {
        groupTableIds.validateExistenceAndDuplication(orderTables);
        validateSize(orderTables);
        validateOrderTableStatus(orderTables);
        orderTables.forEach(orderTable -> orderTable.group(id));
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

    public void ungroup(GroupTables groupTables, TableOrders tableOrders) {
        validateGroupCanBeUngrouped(tableOrders);
        groupTables.ungroup();
    }

    private void validateGroupCanBeUngrouped(TableOrders tableOrders) {
        if (tableOrders.hasCookingOrMealOrder()) {
            throw new IllegalArgumentException("조리 혹은 식사 주문이 존재하는 단체 지정은 단체 지정을 취소할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
