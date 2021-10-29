package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {
    private List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = new ArrayList<>(orderTables);
    }

    private void validate(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTable> getOrderTables() {
        return new ArrayList<>(orderTables);
    }

    public void checkSizeWith(List<OrderTable> other) {
        if (orderTables.size() != other.size()) {
            throw new IllegalArgumentException();
        }
    }
}
