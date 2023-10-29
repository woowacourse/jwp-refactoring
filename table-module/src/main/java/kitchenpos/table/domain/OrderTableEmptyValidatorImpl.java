package kitchenpos.table.domain;

import kitchenpos.order.domain.OrderTableEmptyValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderTableEmptyValidatorImpl implements OrderTableEmptyValidator {

    private OrderTableRepository orderTableRepository;

    public OrderTableEmptyValidatorImpl(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validateEmpty(final Long orderTableId) {
        final OrderTable findOrderTable = orderTableRepository.findOrderTableById(orderTableId);

        if (!findOrderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있지 않은 상태입니다.");
        }
    }
}
