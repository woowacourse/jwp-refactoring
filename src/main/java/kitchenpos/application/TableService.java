package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.NumberOfGuests;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.repository.JpaOrderRepository;
import kitchenpos.repository.JpaOrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final JpaOrderRepository orderRepository;
    private final JpaOrderTableRepository orderTableRepository;

    public TableService(JpaOrderRepository orderRepository, JpaOrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(OrderTableCreateRequest request) {
        OrderTable orderTable = request.toDomain();
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, boolean empty) {
        OrderTable orderTable = orderTableRepository.getById(orderTableId);
        Order order = orderRepository.getByOrderTable(orderTable);

        order.changeOrderTableEmpty(empty);

        return orderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, Integer numberOfGuests) {
        OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeNumberOfGuests(new NumberOfGuests(numberOfGuests));
        return orderTable;
    }
}
