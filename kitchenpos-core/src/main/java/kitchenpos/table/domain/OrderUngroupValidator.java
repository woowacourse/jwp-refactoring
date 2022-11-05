package kitchenpos.table.domain;

import java.util.List;

public interface OrderUngroupValidator {

    void validateOrderStatus(List<Long> ids);
}
