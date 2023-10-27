package kitchenpos.ordertablegroup;

import java.util.List;

public interface OrderTableValidator {

    void validateOrderStatusByOrderTableIds(final List<Long> orderTableIds);

}
