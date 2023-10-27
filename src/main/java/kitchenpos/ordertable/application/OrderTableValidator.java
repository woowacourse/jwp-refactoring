package kitchenpos.ordertable.application;

import java.util.List;

public interface OrderTableValidator {

    void validate(final Long orderTableId, final List<String> orderStatuses);
}
