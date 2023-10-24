package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "tableGroup")
    private List<OrderTable> values = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> values) {
        this.values = values;
    }

    public void ungroup() {
        values.forEach(OrderTable::ungroup);
    }

    public List<OrderTable> getValues() {
        return values;
    }
}
