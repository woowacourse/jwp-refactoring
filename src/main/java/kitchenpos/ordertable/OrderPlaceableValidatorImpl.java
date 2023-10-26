package kitchenpos.ordertable;

import kitchenpos.order.OrderPlaceableValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderPlaceableValidatorImpl implements OrderPlaceableValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderPlaceableValidatorImpl(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validate(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.getBy(orderTableId);
        validateFull(orderTable);
    }

    private void validateFull(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블에서 주문할 수 없습니다");
        }
    }
}
