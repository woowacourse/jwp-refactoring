package kitchenpos.domain.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> values = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> values) {
        values.forEach(this::validateTableGrouping);
        this.values = values;
    }

    private void validateTableGrouping(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTable> getValues() {
        return values;
    }
}
