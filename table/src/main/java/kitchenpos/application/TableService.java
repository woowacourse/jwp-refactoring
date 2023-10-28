package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.request.OrderTableUpdateNumberOfGuestRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableUpdateEmptyRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(OrderTableRepository orderTableRepository, TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public OrderTableResponse create() {
        OrderTable orderTable = new OrderTable(0, false);
        orderTableRepository.save(orderTable);
        return OrderTableResponse.from(orderTable);
    }

    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableUpdateEmptyRequest orderTableRequest) {
        OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.changeEmpty(orderTableRequest.isEmpty(), tableValidator);
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다."));
    }

    public OrderTableResponse changeNumberOfGuests(
            Long orderTableId,
            OrderTableUpdateNumberOfGuestRequest orderTableRequest
    ) {
        OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.changeNumberOfGuest(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }
}
