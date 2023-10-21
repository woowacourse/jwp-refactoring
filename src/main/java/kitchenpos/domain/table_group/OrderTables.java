package kitchenpos.domain.table_group;

import kitchenpos.domain.order_table.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Embeddable
public class OrderTables {

    private static final int MIN_TABLE_GROUP_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY)
    private List<OrderTable> values;

    public OrderTables(final List<OrderTable> values) {
        this.values = values;
    }

    protected OrderTables() {
        this(Collections.emptyList());
    }

    public List<OrderTable> getValues() {
        return values;
    }

    public boolean canNotGroup() {
        return isGroupSize()
                || isHasNotSaved()
                || isHasUnableToGroup();
    }

    private boolean isGroupSize() {
        return CollectionUtils.isEmpty(values) || values.size() < MIN_TABLE_GROUP_SIZE;
    }

    private boolean isHasNotSaved() {
        return values.stream()
                .anyMatch(value -> Objects.isNull(value.getId()));
    }

    private boolean isHasUnableToGroup() {
        return values.stream()
                .anyMatch(value -> !value.isEmpty() || Objects.nonNull(value.getTableGroup()));
    }

    public void updateGroup(final TableGroup tableGroup) {
        values.forEach(value -> {
            value.setTableGroup(tableGroup);
            value.setEmpty(false);
        });
    }
}
