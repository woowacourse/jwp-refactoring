package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.OrderTableResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableVerifier;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableVerifier orderTableVerifier;

    public TableService(
        final OrderTableRepository orderTableRepository,
        final OrderTableVerifier orderTableVerifier
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableVerifier = orderTableVerifier;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest orderTableCreateRequest) {
        return OrderTableResponse
            .from(orderTableRepository.save(orderTableCreateRequest.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return OrderTableResponse.listOf(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(
        final Long orderTableId,
        final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest
    ) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        orderTableVerifier.verifyNotCompletedOrderStatus(orderTableId);

        savedOrderTable.changeEmpty(orderTableChangeEmptyRequest.isEmpty());

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
        final Long orderTableId,
        final OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest
    ) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrderTable
            .changeNumberOfGuests(orderTableChangeNumberOfGuestsRequest.getNumberOfGuests());

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }
}
