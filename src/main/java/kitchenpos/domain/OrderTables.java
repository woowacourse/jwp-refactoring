package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> values = new ArrayList<>();

    public OrderTables(final List<OrderTable> orderTables) {
        values.addAll(orderTables);
    }

    protected OrderTables() {
    }

    public void addAll(final List<OrderTable> orderTables) {
        values.addAll(orderTables);
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public boolean isSmallerThan(final int size) {
        return values.size() < size;
    }

    public boolean anyUsing() {
        return values.stream()
                .anyMatch(OrderTable::isUsing);
    }

    public List<OrderTable> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "OrderTables{" +
                "orderTables=" + values +
                '}';
    }
}
