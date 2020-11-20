package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.verifier.DefaultOrderVerifier;
import kitchenpos.domain.verifier.OrderVerifier;
import kitchenpos.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.repository.OrderTableRepository;

@Service
@Validated
@Transactional
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderVerifier orderVerifier;

    public TableService(OrderTableRepository orderTableRepository,
        DefaultOrderVerifier orderVerifier) {
        this.orderTableRepository = orderTableRepository;
        this.orderVerifier = orderVerifier;
    }

    public OrderTableResponse create(final OrderTableRequest request) {
        return OrderTableResponse.of(orderTableRepository.save(request.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return OrderTableResponse.listOf(orderTableRepository.findAll());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId,
        final OrderTableChangeEmptyRequest request) {

        final OrderTable savedOrderTable = orderVerifier.verifyOrderStatusByTableId(orderTableId);
        savedOrderTable.changeEmpty(request.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
        final OrderTableChangeNumberOfGuestsRequest request) {

        final OrderTable savedOrderTable = orderVerifier.verifyOrderStatusByTableId(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.of(savedOrderTable);
    }
}
