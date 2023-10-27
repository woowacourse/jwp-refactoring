package kitchenpos.ordertablegroup;

import java.util.List;

public interface OrderTableValidator {

    void validateOrderStatus(final List<Long> orderTableIds);

}
