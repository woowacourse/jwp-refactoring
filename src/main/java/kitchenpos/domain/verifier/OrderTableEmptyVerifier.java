package kitchenpos.domain.verifier;

import org.springframework.stereotype.Component;

import kitchenpos.domain.OrderTable;
import kitchenpos.exception.AlreadyEmptyTableException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.repository.OrderTableRepository;

@Component
public class OrderTableEmptyVerifier implements OrderTableVerifier {
    private final OrderTableRepository orderTableRepository;

    public OrderTableEmptyVerifier(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void verify(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new OrderTableNotFoundException(orderTableId));

        if (orderTable.isEmpty()) {
            throw new AlreadyEmptyTableException(orderTableId);
        }
    }
}
