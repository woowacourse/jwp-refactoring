package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderTableCommand;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.OrderAble;
import kitchenpos.domain.OrderTable;
import kitchenpos.dao.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final TableValidator tableValidator;
    private final OrderTableRepository orderTableRepository;

    public TableService(TableValidator tableValidator, OrderTableRepository orderTableRepository) {
        this.tableValidator = tableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableCommand command) {
        OrderTable orderTable = command.toEntity();
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableCommand command) {
        OrderTable orderTable = getOrderTable(orderTableId);
        tableValidator.validateChangeEmpty(orderTable);
        orderTable.decideAvailabilityOfOrderRegistration(OrderAble.of(command.isEmpty()));
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableCommand command) {
        OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.changeNumberOfGuests(command.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }
}
