package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.application.request.OrderTableRequest;
import kitchenpos.application.response.OrderTableResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
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
    public OrderTableResponse create(final OrderTableRequest request) {
        final OrderTable orderTable = OrderTable.of(request.getNumberOfGuests(), request.isEmpty());
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return new OrderTableResponse(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::new)
                .collect(toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable foundOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        foundOrderTable.changeEmptyStatus(request.isEmpty());

        return new OrderTableResponse(foundOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable foundOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        foundOrderTable.updateNumberOfGuests(request.getNumberOfGuests());

        return new OrderTableResponse(foundOrderTable);
    }
}
