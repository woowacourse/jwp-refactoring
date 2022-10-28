package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import kitchenpos.application.dto.request.TableGroupCommand;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    public void validateCreate(TableGroupCommand tableGroupCommand, List<OrderTable> orderTables) {
        List<Long> orderTableIds = tableGroupCommand.getOrderTableId();
        if (orderTableIds.size() < 2) {
            throw new IllegalArgumentException("주문 테이블은 2개 이상이어야 합니다.");
        }

        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("주문 테이블의 수가 다릅니다.");
        }

        for (final OrderTable savedOrderTable : orderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException("빈 주문 테이블이어야 합니다.");
            }
        }
    }
}
