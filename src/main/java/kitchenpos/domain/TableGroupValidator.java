package kitchenpos.domain;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    public void validateCreate(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("주문 테이블의 수가 다릅니다.");
        }
    }
}
