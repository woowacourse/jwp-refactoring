package kitchenpos.table.domain;

import java.util.List;

public interface OrderTableValidator {
    void validateOrderStatus(Long orderTableId, final List<String> orderStatus);
}
