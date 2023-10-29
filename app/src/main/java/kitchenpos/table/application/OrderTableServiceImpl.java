package kitchenpos.table.application;

import kitchenpos.order.application.OrderTableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.persistence.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderTableServiceImpl implements OrderTableService {

    private final OrderTableRepository orderTableRepository;

    public OrderTableServiceImpl(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void checkOrderTableEmpty(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
