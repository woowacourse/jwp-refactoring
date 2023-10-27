package kitchenpos.order.domain;

import kitchenpos.order.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(final Order order) {
        validateEmptyTable(order);
        validateEmptyOrderLineItems(order);
    }

    private void validateEmptyTable(final Order order) {
        final OrderTable orderTable = findOrderTableById(order.getOrderTableId());
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있어 주문을 할 수 없습니다.");
        }
    }

    private OrderTable findOrderTableById(final long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("테이블을 찾을 수 없습니다."));
    }

    private void validateEmptyOrderLineItems(final Order order) {
        if (CollectionUtils.isEmpty(order.getOrderLineItems())) {
            throw new IllegalArgumentException("주문 품목이 없어 주문할 수 없습니다.");
        }
    }
}
