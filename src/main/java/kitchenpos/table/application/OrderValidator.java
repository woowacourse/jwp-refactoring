package kitchenpos.table.application;

import java.util.List;

public interface OrderValidator {

    void validateOrderNotCompleted(Long orderTableId);

    void validateAnyOrdersNotCompleted(List<Long> orderTableIds);
}
