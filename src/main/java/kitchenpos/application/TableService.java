package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderTableEmptyRequest;
import kitchenpos.application.dto.request.OrderTableGuestsRequest;
import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.exception.NotFoundOrderTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.toEntity();
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.create(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> list = orderTableRepository.findAll();

        return list.stream()
                .map(OrderTableResponse::create)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableEmptyRequest orderTableEmptyRequest) {

        OrderTable orderTable = orderTableEmptyRequest.toEntity();

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(NotFoundOrderTableException::new);

        savedOrderTable.changeEmpty(orderTable.isEmpty());

        return OrderTableResponse.create(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableGuestsRequest orderTableGuestsRequest) {

        OrderTable orderTable = orderTableGuestsRequest.toEntity();

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(NotFoundOrderTableException::new);

        savedOrderTable.changeGuests(orderTable.getNumberOfGuests());

        return OrderTableResponse.create(savedOrderTable);
    }
}
