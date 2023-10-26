package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables;

    public OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private void validate(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.canBeGroup()) {
                throw new IllegalArgumentException();
            }
        }
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void leaveGroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.leaveGroup();
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
