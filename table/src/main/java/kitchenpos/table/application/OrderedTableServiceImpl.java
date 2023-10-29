package kitchenpos.table.application;

import kitchenpos.order.application.OrderedTableService;
import org.springframework.stereotype.Component;
import kitchenpos.table.domain.OrderTableRepository;

@Component
public class OrderedTableServiceImpl implements OrderedTableService {
    private final OrderTableRepository orderTableRepository;

    public OrderedTableServiceImpl(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public boolean doesTableExist(final Long orderTableId) {
        return orderTableRepository.existsById(orderTableId);
    }
}
