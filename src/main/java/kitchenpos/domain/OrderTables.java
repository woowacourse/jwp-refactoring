package kitchenpos.domain;

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

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    public OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables) {
        validateTableCount(orderTables);
        this.orderTables = groupTables(orderTables);
    }

    private void validateTableCount(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderTable> groupTables(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::changeToUse)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
