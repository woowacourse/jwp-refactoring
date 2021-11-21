package kitchenpos.order.application;

import kitchenpos.exception.BadRequestException;
import kitchenpos.exception.ErrorType;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderOrderTableValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderOrderTableValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateOrderTable(Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new BadRequestException(ErrorType.TABLE_NOT_FOUND));
        if (orderTable.isEmpty()) {
            throw new BadRequestException(ErrorType.TABLE_EMPTY_ERROR);
        }
    }
}
