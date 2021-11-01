package kitchenpos.application;

import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;

@Service
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;

    public OrderTableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
    }
}
