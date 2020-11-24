package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.OrderTableResponse;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableVerifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderTableDao orderTableDao;
    private final OrderTableVerifier orderTableVerifier;

    public TableService(
        final OrderTableDao orderTableDao,
        final OrderTableVerifier orderTableVerifier
    ) {
        this.orderTableDao = orderTableDao;
        this.orderTableVerifier = orderTableVerifier;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest orderTableCreateRequest) {
        return OrderTableResponse.from(orderTableDao.save(orderTableCreateRequest.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return OrderTableResponse.listOf(orderTableDao.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(
        final Long orderTableId,
        final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest
    ) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        orderTableVerifier.verifyNotCompletedOrderStatus(orderTableId);

        savedOrderTable.changeEmpty(orderTableChangeEmptyRequest.isEmpty());

        return OrderTableResponse.from(orderTableDao.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
        final Long orderTableId,
        final OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest
    ) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrderTable
            .changeNumberOfGuests(orderTableChangeNumberOfGuestsRequest.getNumberOfGuests());

        return OrderTableResponse.from(orderTableDao.save(savedOrderTable));
    }
}
