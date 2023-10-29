package kitchenpos.order.application;

import kitchenpos.order.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.order.application.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.order.application.dto.OrderTableCreateRequest;
import kitchenpos.order.application.dto.OrderTableResponse;
import kitchenpos.order.OrderTable;
import kitchenpos.order.OrderTableRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;

    public OrderTableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);

        orderTable.setEmpty(request.isEmpty());

        return OrderTableResponse.of(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(
            final Long orderTableId,
            final OrderTableChangeNumberOfGuestsRequest request
    ) {
        final OrderTable savedOrderTable = orderTableRepository.getById(orderTableId);

        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }
}
