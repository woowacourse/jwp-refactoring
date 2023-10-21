package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dto.OrderTableEmptyRequest;
import kitchenpos.application.dto.OrderTableGuestRequest;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.application.dto.OrderTablesRequest;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.exception.OrderTableException.ExistsNotCompletionOrderException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
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

        if (orderRepository.existsByOrderTableAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new ExistsNotCompletionOrderException(orderTableId);
        }

        savedOrderTable.changeEmpty(orderTableEmptyRequest.isEmpty());

        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableGuestRequest orderTableGuestRequest) {
        final OrderTable savedOrderTable = orderTableRepository.getById(orderTableId);

        savedOrderTable.changeNumberOfGuest(orderTableGuestRequest.getNumberOfGuests());

        return savedOrderTable;
    }
}
