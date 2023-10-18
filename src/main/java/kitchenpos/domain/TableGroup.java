package kitchenpos.domain;

import static java.util.Objects.nonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this(LocalDateTime.now(), orderTables);
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        orderTables.forEach(orderTable -> orderTable.changeGroup(this));
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private static void validateOrderTables(final List<OrderTable> orderTables) {
        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalArgumentException("orderTables 갯수가 2 이하입니다.");
        }

        final boolean containsInvalidOrderTable = orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty() || nonNull(orderTable.getTableGroup()));

        if (containsInvalidOrderTable) {
            throw new IllegalArgumentException("비어있지 않거나, 이미 그룹화된 테이블을 포함하고 있습니다.");
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

    public void remove(final OrderTable orderTable) {
        orderTables.remove(orderTable);
    }
}
