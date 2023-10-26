package kitchenpos.application.table;

import kitchenpos.application.order.OrderedTableService;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Component;

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
