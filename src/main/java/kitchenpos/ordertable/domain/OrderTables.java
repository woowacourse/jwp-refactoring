package kitchenpos.ordertable.domain;

import org.springframework.util.CollectionUtils;

import java.util.List;

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

        for (OrderTable orderTable : this.orderTables) {
            if (orderTable.isNotEmpty() || orderTable.hasTableGroup()) {
                throw new IllegalArgumentException(String.format("%d번 테이블 : 단체 지정은 중복될 수 없습니다.", orderTable.getId()));
            }
        }
    }

    public boolean isNotSameSizeWith(int size) {
        return this.orderTables.size() == size;
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(this.orderTables);
    }

    public void groupBy(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.groupBy(tableGroup);
        }
    }
}
