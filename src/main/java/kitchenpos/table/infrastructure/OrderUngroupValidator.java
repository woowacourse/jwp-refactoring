package kitchenpos.table.infrastructure;

import java.util.List;

public interface OrderUngroupValidator {

    void validateOrderStatus(List<Long> ids);
}
