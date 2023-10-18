package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.application.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.application.dto.OrderTableCreateRequest;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableCreateRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::from)
                .toList();
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableChangeEmptyRequest request) {
        OrderTable savedOrderTable = orderTableRepository.getById(orderTableId);
        savedOrderTable.setEmpty(request.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableChangeNumberOfGuestsRequest request) {
        OrderTable savedOrderTable = orderTableRepository.getById(orderTableId);
        savedOrderTable.setNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }
}
