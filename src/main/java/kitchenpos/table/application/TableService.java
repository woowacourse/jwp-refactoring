package kitchenpos.table.application;

import java.util.List;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.request.OrderTableGuestNumberRequest;
import kitchenpos.dto.request.OrderTableSetEmptyRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest orderTableCreateRequest) {
        final OrderTable orderTable = new OrderTable(orderTableCreateRequest.getNumberOfGuests(), orderTableCreateRequest.isEmpty());
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableSetEmptyRequest orderTableSetEmptyRequest) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.setEmpty(orderTableSetEmptyRequest.isEmpty());
        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final OrderTableGuestNumberRequest orderTableGuestNumberRequest) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        final int numberOfGuests = orderTableGuestNumberRequest.getNumberOfGuests();
        savedOrderTable.setNumberOfGuests(numberOfGuests);
        return savedOrderTable;
    }

    private OrderTable findOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
    }
}
