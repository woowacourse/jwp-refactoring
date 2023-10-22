package kitchenpos.table_group;

import java.util.List;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.order.OrderTable;

public class GroupedOrderTables {

    private static final int MIN_ORDER_TABLE_SIZE = 2;

    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> values;

    protected GroupedOrderTables() {
    }

    public GroupedOrderTables(List<OrderTable> values) {
        validate(values);
        this.values = values;
    }

    public void group(TableGroup tableGroup) {
        values.forEach(it -> it.group(tableGroup.getId()));
    }

    public void ungroup() {
        values.forEach(OrderTable::unGroup);
    }

    private void validate(List<OrderTable> values) {
        if (values.size() < MIN_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException(String.format("테이블 그룹은 최소 %s개 이상의 테이블이 필요합니다.", MIN_ORDER_TABLE_SIZE));
        }
    }

    public List<OrderTable> getValues() {
        return values;
    }
}
