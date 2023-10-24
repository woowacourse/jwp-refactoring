package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public boolean isNotGroupable() {
        return isNotGroupSize()
                || isHasNotSaved()
                || isHasUnableToGroup();
    }

    private boolean isNotGroupSize() {
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

    public List<Long> getOrderTableIds() {
        return values.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void ungroup() {
        values.forEach(value -> {
            value.setTableGroup(null);
            value.setEmpty(false);
        });
    }
}
