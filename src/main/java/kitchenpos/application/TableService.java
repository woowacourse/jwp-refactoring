package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.EmptyRequest;
import kitchenpos.dto.request.NumberOfGuestsRequest;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableDao orderTableDao;

    public TableService(
            final OrderRepository orderRepository,
            final OrderTableDao orderTableDao
    ) {
        this.orderRepository = orderRepository;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        return OrderTableResponse.of(orderTableDao.save(request.toEntity()));
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final EmptyRequest request) {
        final OrderTable savedOrderTable = getOrderTableById(orderTableId);
        validateExistsByOrderTableIdAndOrderStatusIn(orderTableId);
        savedOrderTable.changeEmpty(request.isEmpty());

        return OrderTableResponse.of(orderTableDao.save(savedOrderTable));
    }

    private OrderTable getOrderTableById(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateExistsByOrderTableIdAndOrderStatusIn(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, List.of(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final NumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = getOrderTableById(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.of(orderTableDao.save(savedOrderTable));
    }
}
