package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(orderTables);
    }

    public boolean isSmallerThen(final int value) {
        return orderTables.size() < value;
    }

    public boolean isUsing() {
        return orderTables
            .stream()
            .anyMatch(OrderTable::isUsing);
    }

    public void unBindGroups() {
        orderTables.forEach(OrderTable::unBindGroup);
    }
    public List<Long> getOrderTableIds() {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
