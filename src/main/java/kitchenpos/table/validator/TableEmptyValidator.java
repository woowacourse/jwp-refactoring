package kitchenpos.table.validator;

import kitchenpos.order.validator.OrderTableValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class TableEmptyValidator implements OrderTableValidator {

    private final OrderTableRepository orderTableRepository;

    public TableEmptyValidator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validateOrderTableNotEmpty(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있으면 주문을 생성할 수 없다.");
        }
    }
}
