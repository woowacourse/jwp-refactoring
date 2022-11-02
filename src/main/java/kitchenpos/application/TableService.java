package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(OrderTableCreateRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(Long orderTableId, boolean empty) {
        OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeEmpty(empty);
        return OrderTableResponse.from(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(Long orderTableId, int numberOfGuests) {
        OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }
}
