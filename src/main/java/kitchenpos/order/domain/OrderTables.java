package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTables {
    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        validate();
    }

    private void validate() {
        if (this.orderTables.isEmpty() || this.orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블을 2개 이상 입력해주세요.");
        }
    }

    public void groupBy(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.groupBy(tableGroup);
        }
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public boolean isNotSameSizeWith(int size) {
        return this.orderTables.size() == size;
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(this.orderTables);
    }

    public List<Long> getOrderTableIds() {
        return this.orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
