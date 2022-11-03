package kitchenpos.table.application;

import org.springframework.stereotype.Component;

@Component
public interface OrderStatusValidator {

    void validateChangeEmpty(Long orderTableId);
}
