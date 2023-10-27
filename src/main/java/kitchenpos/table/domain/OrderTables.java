package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

public class OrderTables {

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<OrderTable> values;

    public OrderTables() {
    }

    public OrderTables(final List<OrderTable> values) {
        validateSize(values);
        this.values = values;
    }

    private void validateSize(final List<OrderTable> values) {
        if (CollectionUtils.isEmpty(values) || values.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public boolean containEmptyOrderTable() {
        return values.stream()
                .anyMatch(OrderTable::isEmpty);
    }

    public void groupAll(final TableGroup tableGroup) {
        for (final OrderTable value : values) {
            value.group(tableGroup);
        }
    }

    public List<OrderTable> getValues() {
        return values;
    }
}
