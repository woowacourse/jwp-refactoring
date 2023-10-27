package kitchenpos.ordertable.domain;

import java.util.Optional;
import kitchenpos.order.domain.OrderTableValidator;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;

public class OrderTableValidatorImpl implements OrderTableValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderTableValidatorImpl(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validateForOrder(final Long orderTableId) {
        final Optional<OrderTable> orderTable = orderTableRepository.findById(orderTableId);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문하려는 테이블이 존재하지 않습니다.");
        }
        if (orderTable.get().isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블은 주문할 수 없습니다.");
        }
    }
}
