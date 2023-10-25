package kitchenpos.order.application;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.order.application.dto.OrderTableEmptyRequest;
import kitchenpos.order.application.dto.OrderTableGuestRequest;
import kitchenpos.order.application.dto.OrderTableRequest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.exception.OrderTableException.ExistsNotCompletionOrderException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
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
    public OrderTable create(final OrderTableRequest orderTableRequest) {
        return orderTableRepository.save(orderTableRequest.toEntity());
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableEmptyRequest orderTableEmptyRequest) {
        final OrderTable savedOrderTable = orderTableRepository.getById(orderTableId);

        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
                Collections.singletonList(savedOrderTable), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new ExistsNotCompletionOrderException(orderTableId);
        }

        savedOrderTable.changeEmpty(orderTableEmptyRequest.isEmpty());

        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final OrderTableGuestRequest orderTableGuestRequest) {
        final OrderTable savedOrderTable = orderTableRepository.getById(orderTableId);

        savedOrderTable.changeNumberOfGuest(orderTableGuestRequest.getNumberOfGuests());

        return savedOrderTable;
    }
}
