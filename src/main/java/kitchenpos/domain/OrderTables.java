package kitchenpos.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroupId")
    private List<OrderTable> orderTables;

    protected OrderTables() {

    }

    public OrderTables(OrderTable... orderTables) {
        this(Arrays.asList(orderTables));
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void connect(TableGroup tableGroup) {
        validateMinimumSize();
        validateSameSize(tableGroup.getOrderTables());
        validateGroupable();
        setTableGroup(tableGroup);
    }

    public void validateMinimumSize() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public void validateSameSize(OrderTables other) {
        if (orderTables.size() != other.orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    public void validateGroupable() {
        for (final OrderTable orderTable : orderTables) {
            validateEachOrderTableGroupable(orderTable);
        }
    }

    private void validateEachOrderTableGroupable(OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    public void upGroupAll() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    public List<OrderTable> toList() {
        return Collections.unmodifiableList(orderTables);
    }

    public List<Long> getIds() {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    public void setTableGroup(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.joinGroup(tableGroup.getId());
        }
    }
}
