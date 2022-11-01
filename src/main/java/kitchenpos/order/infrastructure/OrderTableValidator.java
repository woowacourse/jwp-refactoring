package kitchenpos.order.infrastructure;

import org.springframework.stereotype.Component;

@Component
public interface OrderTableValidator {

    void validateEmptyTable(Long orderTableId);
}
