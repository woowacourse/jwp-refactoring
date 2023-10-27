package kitchenpos.order.application;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.dto.OrderTableEmptyRequest;
import kitchenpos.order.application.dto.OrderTableGuestRequest;
import kitchenpos.order.application.dto.OrderTableRequest;
import kitchenpos.order.application.dto.OrderTableResponse;
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
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.from(orderTableRepository.save(orderTableRequest.toEntity()));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
                                          final OrderTableEmptyRequest orderTableEmptyRequest) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);

        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
                Collections.singletonList(orderTable), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new ExistsNotCompletionOrderException(orderTableId);
        }

        orderTable.changeEmpty(orderTableEmptyRequest.isEmpty());

        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableGuestRequest orderTableGuestRequest) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);

        orderTable.changeNumberOfGuest(orderTableGuestRequest.getNumberOfGuests());

        return OrderTableResponse.from(orderTable);
    }
}
