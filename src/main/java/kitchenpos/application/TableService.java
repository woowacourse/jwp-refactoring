package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderDao orderDao, final OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        return OrderTableResponse.from(orderTableRepository.save(request.toOrderTable()));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        // todo : order in order table ?
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        savedOrderTable.changeEmpty(empty);
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }
}
