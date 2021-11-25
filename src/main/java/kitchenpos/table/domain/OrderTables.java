package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class OrderTables {

    private final List<OrderTable> orderTables;

    private OrderTables(List<OrderTable> orderTables) {
        validateNotEnoughSize(orderTables);
        this.orderTables = orderTables;
    }

    private void validateNotEnoughSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("orderTables는 2개 이상의 size여야 합니다.");
        }
    }

    public static OrderTables create(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(toList());
    }

    public void validateSameSize(OrderTables orderTables) {
        if (this.getSize() != orderTables.getSize()) {
            throw new IllegalArgumentException("입력받은 테이블 수와 비교대상에 등록된 테이블 수가 다릅니다.");
        }
    }

    public void group(TableGroup tableGroup) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.group(tableGroup);
        }
    }

    public void ungroup(TableValidator tableValidator) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup(tableValidator);
        }
    }

    public int getSize() {
        return orderTables.size();
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }
}
