package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    public static final int MIN_ORDER_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroupId")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    private TableGroup(
            final Long id,
            final LocalDateTime createdDate,
            final List<OrderTable> orderTables
    ) {
        validateNumberOfOrderTable(orderTables);
        validateOrderTableStatus(orderTables);
        updateTableGroupInOrderTable(orderTables);

        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(
            final LocalDateTime createdDate,
            final List<OrderTable> orderTables
    ) {
        this(null, createdDate, orderTables);
    }

    private void validateNumberOfOrderTable(
            final List<OrderTable> orderTables
    ) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException("order table 수는 2 이상이어야 합니다.");
        }
    }

    private void validateOrderTableStatus(
            final List<OrderTable> orderTables
    ) {
        orderTables.stream()
                .filter(orderTable -> !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId()))
                .findAny()
                .ifPresent(orderTable -> {
                    throw new IllegalArgumentException("orderTable 은 비어있어야 하고, 소속된 table group이 없어야 합니다.");
                });
    }

    public void updateTableGroupInOrderTable(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        orderTables.forEach(orderTable -> orderTable.updateTableGroup(id));
    }

    public void ungroup() {
        orderTables.forEach(orderTable -> orderTable.updateTableGroup(null));
        orderTables = null;
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
