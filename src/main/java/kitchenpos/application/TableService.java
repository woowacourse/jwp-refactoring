package kitchenpos.application;

import java.util.List;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.ui.dto.table.CreateOrderTableRequest;
import kitchenpos.ui.dto.table.UpdateTableGuestRequest;
import kitchenpos.ui.dto.table.UpdateTableStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final CreateOrderTableRequest createOrderTableRequest) {
        final OrderTable orderTable = createOrderTableRequest.toDomain();
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final UpdateTableStatusRequest updateTableStatusRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        orderRepository.findAllByOrderTableId(orderTableId)
                .forEach(Order::validateUncompleted);

        orderTable.updateStatus(updateTableStatusRequest.getEmpty());
        return orderTableRepository.save(orderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final UpdateTableGuestRequest updateTableGuestRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        final int numberOfGuests = updateTableGuestRequest.getGuests();
        orderTable.updateNumberOfGuests(numberOfGuests);

        return orderTableRepository.save(orderTable);
    }
}
