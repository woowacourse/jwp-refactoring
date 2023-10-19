package kitchenpos.domain.order;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private final List<OrderTable> orderTables = new ArrayList<>();

    public void addAll(final List<OrderTable> toAddOrderTables) {
        this.orderTables.addAll(toAddOrderTables);
    }

    public int size() {
        return orderTables.size();
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
