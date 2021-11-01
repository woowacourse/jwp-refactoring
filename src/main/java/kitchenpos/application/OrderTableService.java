package kitchenpos.application;

import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;

    public OrderTableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }


    public OrderTable findOrderTable(Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return orderTable;
    }
}
