package kitchenpos.ordertable.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderTableValidator {

    void validateOrdersToUngroup(final List<Long> orderTableIds);

    void validateOrderStatusIsCompletion(final Long orderTableId);
}
