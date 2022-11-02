package kitchenpos.order.validator;

import java.util.List;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.exception.InvalidTableOrderException;
import kitchenpos.order.exception.MenuNotEnoughException;
import kitchenpos.ordertable.repository.TableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private final TableRepository tableRepository;

    public OrderValidator(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public void validateCreation(Long orderTableId, List<OrderLineItem> orderLineItems) {
        validateOrderTable(orderTableId);
        validateMenuMinSize(orderLineItems);
    }

    private void validateOrderTable(Long orderTableId) {
        if (!tableRepository.existsByIdAndAndEmptyIsFalse(orderTableId)) {
            throw new InvalidTableOrderException();
        }
    }

    private void validateMenuMinSize(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new MenuNotEnoughException();
        }
    }
}
