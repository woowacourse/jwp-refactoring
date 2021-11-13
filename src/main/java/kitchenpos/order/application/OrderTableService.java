package kitchenpos.order.application;

import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.order.domain.OrderTable;
import org.springframework.stereotype.Service;

@Service
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;

    public OrderTableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("해당 orderTable이 존재하지 않습니다."));
    }
}
