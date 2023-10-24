package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    private TableGroup(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
        this.orderTables = new ArrayList<>();
    }

    public static TableGroup createWithOrderTables(final List<OrderTable> orderTables) {
        validate(orderTables);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        tableGroup.addOrderTables(orderTables);
        return tableGroup;
    }

    private static void validate(final List<OrderTable> orderTables) {
        validateNotEmptyOrderTables(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.validateEmptyTable();
        }
    }

    private static void validateNotEmptyOrderTables(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public TableGroup(final Long id,
                      final LocalDateTime createdDate,
                      final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public void addOrderTables(final List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            addOrderTable(orderTable);
        }
    }

    public void addOrderTable(final OrderTable orderTable) {
        orderTable.setEmpty(false);
        orderTable.dependOn(this);
        orderTables.add(orderTable);
    }

    public void ungroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.detachFromGroup();
        }
        orderTables.clear();
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TableGroup{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", orderTablesSize=" + orderTables.size() +
                '}';
    }
}
