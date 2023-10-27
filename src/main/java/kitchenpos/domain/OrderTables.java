package kitchenpos.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> items = new ArrayList<>();

    protected OrderTables() {
    }

    private OrderTables(final List<OrderTable> items) {
        this.items = items;
    }

    public static OrderTables from(final List<OrderTable> orderTables) {
        validateTableSize(orderTables);
        validateStatus(orderTables);
        return new OrderTables(orderTables);
    }

    private static void validateTableSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("단체 지정 시 테이블은 2개 이상이어야 합니다.");
        }
    }

    private static void validateStatus(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException("빈 테이블, 단체 지정이 되어 있지 않은 테이블만 단체 지정이 가능합니다.");
            }
        }
    }

    public void group(TableGroup tableGroup) {
        for (final OrderTable orderTable : items) {
            orderTable.updateEmpty(false);
            orderTable.updateTableGroup(tableGroup);
        }
    }

    public void ungroup() {
        items.forEach(OrderTable::unGroup);
    }

    public List<OrderTable> getItems() {
        return Collections.unmodifiableList(items);
    }
}
