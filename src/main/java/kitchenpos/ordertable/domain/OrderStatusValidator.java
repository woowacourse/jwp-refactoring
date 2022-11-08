package kitchenpos.ordertable.domain;

import java.util.List;

public interface OrderStatusValidator {

    void validateStatusChange(List<Long> orderTableIds);
}
