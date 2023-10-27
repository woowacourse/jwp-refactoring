package kitchenpos.domain.table;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {

    private static final int MIN_TABLE_GROUP_SIZE = 2;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> values;

    public OrderTables(final List<OrderTable> values) {
        this.values = values;
    }

    protected OrderTables() {
        this(Collections.emptyList());
    }

    public void group() {
        validateGroupable();

        values.forEach(value -> value.setEmpty(false));
    }

    private void validateGroupable() {
        validateMinGroupableSize();

        values.forEach(OrderTable::validateGroupable);
    }

    private void validateMinGroupableSize() {
        if (values.isEmpty() || values.size() < MIN_TABLE_GROUP_SIZE) {
            throw new IllegalArgumentException(MIN_TABLE_GROUP_SIZE + "이상의 테이블을 지정해 주세요.");
        }
    }

    public void ungroup() {
        values.forEach(OrderTable::ungroup);
    }

    public List<OrderTable> getValues() {
        return values;
    }

    public List<Long> getOrderTableIds() {
        return values.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public boolean isSameSize(final int size) {
        return values.size() == size;
    }
}
