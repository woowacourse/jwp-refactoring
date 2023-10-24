package kitchenpos.tablegroup.domain;

import java.util.List;
import java.util.Optional;
import kitchenpos.table.domain.OrderTable;
import org.springframework.util.CollectionUtils;

public class GroupedTables {

    private static final int MIN_ORDER_TABLE_SIZE = 2;

    private List<OrderTable> orderTables;

    private GroupedTables() {
    }

    private GroupedTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static GroupedTables createForGrouping(List<OrderTable> orderTables) {
        validateForGrouping(orderTables);

        return new GroupedTables(orderTables);
    }

    private static void validateForGrouping(List<OrderTable> orderTables) {
        validateSize(orderTables);
        validateDuplicateGroup(orderTables);
        validateEmpty(orderTables);
    }

    private static void validateSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException("그룹화 할 테이블 개수는 2 이상이어야 합니다");
        }
    }

    private static void validateDuplicateGroup(List<OrderTable> orderTables) {
        Optional<OrderTable> groupedOrderTable = orderTables.stream()
                .filter(OrderTable::isGrouped)
                .findAny();

        if (groupedOrderTable.isPresent()) {
            throw new IllegalArgumentException("이미 다른 그룹에 속해있는 테이블이 존재합니다.");
        }
    }

    private static void validateEmpty(List<OrderTable> orderTables) {
        Optional<OrderTable> emptyOrderTable = orderTables.stream()
                .filter(orderTable -> !orderTable.isEmpty())
                .findAny();

        if (emptyOrderTable.isPresent()) {
            throw new IllegalArgumentException("주문 가능한 상태의 테이블이 존재합니다.");
        }
    }

    public static GroupedTables createFourUngrouping(List<OrderTable> orderTables) {
        return new GroupedTables(orderTables);
    }

    public void group(Long tableGroupId) {
        orderTables.forEach(orderTable -> orderTable.group(tableGroupId));
    }

    public void ungroup(TableGroupValidator tableGroupValidator) {
        tableGroupValidator.validateUnableUngrouping(orderTables);

        orderTables.forEach(OrderTable::ungroup);
    }


    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

}
