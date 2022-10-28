package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> values = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> values) {
        this.values = values;
    }

    public static OrderTables group(List<OrderTable> values) {
        validate(values);
        return new OrderTables(values);
    }

    private static void validate(List<OrderTable> values) {
        if (values.size() < 2) {
            throw new IllegalArgumentException("주문 테이블은 2개 이상이어야 합니다.");
        }

        for (OrderTable orderTable : values) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException("빈 주문 테이블이어야 합니다.");
            }
        }
    }

    public void ungroup() {
        for (OrderTable orderTable : values) {
            orderTable.unGroup();
        }
    }

    public List<Long> getIds() {
        return values.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
