package kitchenpos.table.application;

import kitchenpos.order.application.OrderTableService;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderTableServiceImpl implements OrderTableService {

    private final OrderTableRepository orderTableRepository;

    public OrderTableServiceImpl(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public boolean isOrderTableNotExist(final Long orderTableId) {
        return !orderTableRepository.existsById(orderTableId);
    }
}
