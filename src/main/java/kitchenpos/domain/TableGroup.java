package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("단체 지정할 테이블이 2개보다 작을 수 없습니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            validateOrderTableEmptyOrNotExist(orderTable);
        }

        final TableGroup tableGroup = new TableGroup(createdDate, orderTables);
        tableGroup.mergeOrderTables();

        return tableGroup;
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            checkOrderStatus(orderTable);
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    public void mergeOrderTables() {
        for (OrderTable orderTable : orderTables) {
            orderTable.merge(id);
        }
    }

    private static void checkOrderStatus(final OrderTable orderTable) {
        if (orderTable.containsCookingOrMealOrder()) {
            throw new IllegalArgumentException("이미 테이블의 음식을 준비중이거나 식사중이면 단체 지정을 해제할 수 없습니다.");
        }
    }

    private static void validateOrderTableEmptyOrNotExist(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("이미 테이블이 단체 지정되어있거나 비어있지 않으면 단체 지정할 수 없습니다.");
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
