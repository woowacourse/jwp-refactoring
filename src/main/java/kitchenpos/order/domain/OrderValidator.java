package kitchenpos.order.domain;

import java.util.List;
import java.util.Objects;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Long orderTableId, List<OrderLineItem> orderLineItems) {
        checkExistOrderTable(orderTableId);
        checkOrderLineItemsIsNotNull(orderLineItems);
    }

    private void checkOrderLineItemsIsNotNull(List<OrderLineItem> orderLineItems) {
        if (Objects.isNull(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private void checkExistOrderTable(Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
