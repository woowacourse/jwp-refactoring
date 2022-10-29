package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroupId", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void changeGroup(final Long tableGroupId) {
        for (OrderTable orderTable : orderTables) {
            orderTable.validateEmpty();
            orderTable.addTableGroupId(tableGroupId);
            orderTable.changeEmpty(false);
        }
        validateOrderTables(orderTables);
    }

    public void changeUngroups() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.addTableGroupId(null);
            orderTable.changeEmpty(true);
        }
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        validateSize(orderTables);
        validateOrderTable(orderTables);

    }

    private void validateSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTable(final List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.validateNotEmpty();
            orderTable.validateNotTableGroupId();
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
