package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.request.OrderTableCreateRequest;
import kitchenpos.table.OrderTable;
import kitchenpos.table.persistence.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderTableService {

    private final OrderTableValidator orderTableValidator;
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(OrderTableValidator orderTableValidator, OrderTableRepository orderTableRepository) {
        this.orderTableValidator = orderTableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(OrderTableCreateRequest request) {
        return orderTableRepository.save(new OrderTable(request.getNumberOfGuests(), request.getEmpty()));
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, boolean changedStatus) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
        orderTableValidator.validateChangeEmpty(orderTableId);
        savedOrderTable.changeEmpty(changedStatus);
        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, int numberOfGuests) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문테이블입니다."));
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return orderTableRepository.save(savedOrderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }
}
