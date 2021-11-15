package kitchenpos.order.application;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderTableValidator;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TableService {

    private final OrderTableValidator orderTableValidator;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableValidator orderTableValidator, OrderTableRepository orderTableRepository) {
        this.orderTableValidator = orderTableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(OrderTableRequest request) {
        OrderTable orderTable = request.toEntity();
        orderTableRepository.save(orderTable);
        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.listOf(orderTableRepository.findAll());
    }

    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableRequest request) {
        OrderTable orderTable = getOrderTableById(orderTableId);
        orderTable.changeEmptyStatus(orderTableValidator, request.isEmpty());
        return OrderTableResponse.of(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableRequest request) {
        OrderTable orderTable = getOrderTableById(orderTableId);
        orderTable.changeNumberOfGuests(orderTableValidator, request.getNumberOfGuests());
        return OrderTableResponse.of(orderTable);
    }

    private OrderTable getOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }
}
