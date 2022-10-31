package kitchenpos.application.jpa;

import kitchenpos.domain.entity.OrderTable;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class TableServiceAssistant {

    private OrderTableRepository orderTableRepository;

    public TableServiceAssistant(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable findTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
