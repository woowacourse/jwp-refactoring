package kitchenpos.application;

import kitchenpos.application.exception.OrderTableNotFoundException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.CreateOrderTableRequest;
import kitchenpos.ui.dto.request.UpdateOrderTableEmptyRequest;
import kitchenpos.ui.dto.request.UpdateOrderTableNumberOfGuestsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final CreateOrderTableRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());

        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final UpdateOrderTableEmptyRequest request) {
        final OrderTable persistOrderTable = orderTableRepository.findById(orderTableId)
                                                                 .orElseThrow(OrderTableNotFoundException::new);
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        persistOrderTable.changeEmptyStatus(orders, request.isEmpty());

        return persistOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(
            final Long orderTableId,
            final UpdateOrderTableNumberOfGuestsRequest request
    ) {
        final OrderTable persistOrderTable = orderTableRepository.findById(orderTableId)
                                                                 .orElseThrow(OrderTableNotFoundException::new);

        persistOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return persistOrderTable;
    }
}
