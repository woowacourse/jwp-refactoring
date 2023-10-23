package kitchenpos.domain.table;

import kitchenpos.application.exception.OrderTableAppException.NotFoundOrderTableException;
import kitchenpos.domain.order.OrderTableChangeService;
import kitchenpos.domain.order.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderTableChangeServiceImpl implements OrderTableChangeService {

    private final OrderTableRepository orderTableRepository;

    public OrderTableChangeServiceImpl(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public boolean isNotEmpty(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(NotFoundOrderTableException::new);

        return orderTable.isEmpty();
    }
}
