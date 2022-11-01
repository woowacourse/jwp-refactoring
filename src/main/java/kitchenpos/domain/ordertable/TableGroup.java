package kitchenpos.domain.ordertable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    private static final int MIN_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<OrderTable> orderTables;

    private TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validateTables(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup ofNew(final List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup(null, LocalDateTime.now(), orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.joinGroup(tableGroup);
        }

        return tableGroup;
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }

    }

    private void validateTables(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_TABLE_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getOrderTableIds() {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(orderTableIds);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    protected TableGroup() {
    }
}
