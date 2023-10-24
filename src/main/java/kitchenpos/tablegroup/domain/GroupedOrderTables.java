package kitchenpos.tablegroup.domain;

import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.table.domain.OrderTable;
import org.springframework.util.CollectionUtils;

@Embeddable
public class GroupedOrderTables {

    private static final int MIN_ORDER_TABLE_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables;

    protected GroupedOrderTables() {
    }

    private GroupedOrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static GroupedOrderTables from(List<OrderTable> orderTables) {
        validate(orderTables);

        return new GroupedOrderTables(orderTables);
    }

    private static void validate(List<OrderTable> orderTables) {
        validateSize(orderTables);
        validateDuplicateGroup(orderTables);
        validateEmpty(orderTables);
    }

    private static void validateSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException("그룹화 할 테이블 개수는 2 이상이어야 합니다");
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

    private static void validateDuplicateGroup(List<OrderTable> orderTables) {
        Optional<OrderTable> groupedOrderTable = orderTables.stream()
                .filter(OrderTable::isGrouped)
                .findAny();

        if (groupedOrderTable.isPresent()) {
            throw new IllegalArgumentException("이미 다른 그룹에 속해있는 테이블이 존재합니다.");
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

}
