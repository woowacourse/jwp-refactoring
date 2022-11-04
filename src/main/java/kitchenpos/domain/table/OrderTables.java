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

    public OrderTables(final List<OrderTable> values, final TableGroup tableGroup) {
        values.forEach(this::validateTableGrouping);
        addAll(values, tableGroup);
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

    public void addAll(final List<OrderTable> orderTables, final TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.joinTableGroup(tableGroup));
        this.values.addAll(orderTables);
    }
}
