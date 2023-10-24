package kitchenpos.tablegroup.domain.vo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.ordertable.domain.OrderTable;
import org.hibernate.annotations.BatchSize;

@Embeddable
public class OrderTables {
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderTable> orderTableItems = new ArrayList<>();

    public void addAll(final List<OrderTable> orderTables) {
        this.orderTableItems.addAll(orderTables);
    }

    public List<OrderTable> orderTableItems() {
        return orderTableItems;
    }
}
