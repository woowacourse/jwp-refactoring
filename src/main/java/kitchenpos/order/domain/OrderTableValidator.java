package kitchenpos.order.domain;

import org.springframework.stereotype.Component;

@Component
public interface OrderTableValidator {

    void validateEmptyTable(Long orderTableId);
}
