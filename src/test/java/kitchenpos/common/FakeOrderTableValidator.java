package kitchenpos.common;

import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.validator.OrderTableValidator;
import kitchenpos.domain.tablegroup.TableGroup;

public class FakeOrderTableValidator implements OrderTableValidator {
    @Override
    public void validateAbleToChangeEmpty(final boolean empty, final OrderTable orderTable) {
        if (empty && orderTable.isGrouped()) {
            throw new IllegalArgumentException("그룹으로 지정된 테이블은 빈 상태로 변경할 수 없습니다.");
        }
    }

    @Override
    public void validateAbleToUngroup(final TableGroup tableGroup) {
    }
}
