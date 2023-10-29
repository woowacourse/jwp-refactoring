package kitchenpos.table.domain;

import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderCompletedValidator orderCompletedValidator;

    public OrderTableValidator(
            final OrderCompletedValidator orderCompletedValidator
    ) {
        this.orderCompletedValidator = orderCompletedValidator;
    }

    public void canChangeEmpty(final Long tableGroupId, final Long orderTableId) {
        validateGroupedTable(tableGroupId);
        orderCompletedValidator.validateCompleted(orderTableId);
    }

    private void validateGroupedTable(final Long tableGroupId) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("테이블이 그룹에 속해있으면 빈 테이블로 변경할 수 없습니다.");
        }
    }
}
