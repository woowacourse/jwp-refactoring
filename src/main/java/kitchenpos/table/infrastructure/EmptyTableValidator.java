package kitchenpos.table.infrastructure;

import kitchenpos.order.domain.OrderTableValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class EmptyTableValidator implements OrderTableValidator {

    private final OrderTableRepository orderTableRepository;

    public EmptyTableValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validateEmptyTable(Long orderTableId) {
        OrderTable orderTable = getOrderTable(orderTableId);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블입니다.");
        }
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }
}
