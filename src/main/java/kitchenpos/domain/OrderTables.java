package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.REMOVE;

@Embeddable
public class OrderTables {

    private static final int MIN_TABLE_GROUP_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup", cascade = REMOVE)
    private List<OrderTable> values;

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> values) {
        this.values = values;
    }

    public void validateOrderTablesSize() {
        if (CollectionUtils.isEmpty(values) || values.size() < MIN_TABLE_GROUP_SIZE) {
            throw new IllegalArgumentException("OrderTables의 크기가 0 또는 음수일 수 없습니다.");
        }
    }

    public List<OrderTable> getValues() {
        return values;
    }

    public List<Long> getValuesId() {
        return values.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void validateOrderTablesSizeEqualToSavedOrderTablesSize(final List<OrderTable> savedOrderTables) {
        if (values.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("입력받은 OrderTables와 저장된 OrderTables의 크기가 다릅니다.");
        }
    }

    public void updateInfo() {
        for (final OrderTable orderTable : values) {
            orderTable.updateEmpty(false);
            orderTable.updateTableGroup(null);
        }
    }

}

