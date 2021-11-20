package kitchenpos.domain;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import org.springframework.util.CollectionUtils;

public class OrderTables {

    private final List<OrderTable> orderTables;

    private OrderTables(List<OrderTable> orderTables) {
        validateNotEnoughSize(orderTables);
        this.orderTables = orderTables;
    }

    private void validateNotEnoughSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("orderTables가 비어있거나 2개 이하입니다.");
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
            throw new IllegalArgumentException("입력받은 테이블 수와 DB에 등록된 테이블 수가 다릅니다.");
        }
    }

    public void group(TableGroup tableGroup) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.group(tableGroup);
        }
    }

    public void ungroup(){
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    public int getSize() {
        return orderTables.size();
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }
}
