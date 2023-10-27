package kitchenpos.ordertable.domain;

import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    public OrderTable changeEmpty(final OrderTable orderTable, final boolean afterState) {
        if (orderTable.getTableGroupId() != null) {
            throw new IllegalArgumentException("그룹화된 테이블은 빈 여부를 변경할 수 없습니다.");
        }
        orderTable.changeEmpty(afterState);
        return orderTable;
    }
}
