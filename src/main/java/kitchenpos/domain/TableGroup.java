package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.util.CollectionUtils;

@Table(name="table_group")
@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    public TableGroup() {}

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        validateMoreThanTwoOrderTable(orderTables);
        validateIsEmptyAndNotContainTableGroup(orderTables);
        enrollTableGroupToOrderTable(orderTables);
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void validateMoreThanTwoOrderTable(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateIsEmptyAndNotContainTableGroup(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            validateOrderTableIsEmptyAndNotContainTableGroup(orderTable);
        }
    }

    private void validateOrderTableIsEmptyAndNotContainTableGroup(OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }
    }

    private void enrollTableGroupToOrderTable(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.enrollTableGroup(this);
            orderTable.full();
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

    public void deleteOrderTable(OrderTable orderTable) {
        this.orderTables.remove(orderTable);
        orderTable.deleteTableGroup();
        orderTable.empty();
    }

    public boolean hasOrderTableWhichStatusIsCookingOrMeal() {
        return orderTables.stream()
            .anyMatch(OrderTable::isCookingOrMeal);
    }
}
