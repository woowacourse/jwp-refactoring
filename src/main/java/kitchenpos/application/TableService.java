package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.request.OrderTableUpdateGuestRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.OrderTablesResponse;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.of(savedOrderTable);
    }

    public OrderTablesResponse list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return OrderTablesResponse.of(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableUpdateEmptyRequest request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTable.changeToEmpty(request.isEmpty());
        return OrderTableResponse.of(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableUpdateGuestRequest request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTable.changeGuests(request.getNumberOfGuests());
        return OrderTableResponse.of(orderTable);
    }
}
