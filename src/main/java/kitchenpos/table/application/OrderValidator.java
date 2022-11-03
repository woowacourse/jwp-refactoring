package kitchenpos.table.application;

import java.util.List;

public interface OrderValidator {

    void ValidateOrderTableId(Long orderTableId);

    void ValidateOrderTableIds(List<Long> orderTableIds);
}
